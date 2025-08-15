package com.project.service.impl;

import com.project.dao.GoodsDao;
import com.project.dto.good.GoodStockDTO;
import com.project.mapper.GoodsMapper;
import com.project.model.GoodsModel;
import com.project.service.GoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodServiceImp implements GoodService {

    private final GoodsDao goodsDao;
    private final GoodsMapper goodsMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private String stockKey(String id){ return "stock:" + id; }
    private String detailKey(String id){ return "goods:detail:" + id; }
    /**
     * 添加商品（带参数校验）
     */
    @Override
    @Transactional
    public boolean addGood(String goodName, String goodSize, double goodPrice, int goodNum) {
        // 参数校验
        if (goodName == null || goodName.isEmpty() || goodName.length() > 20) {
            return false;
        }
        if (goodSize == null || goodSize.isEmpty()) {
            return false;
        }
        if (goodPrice <= 0) {
            return false;
        }
        if (goodNum < 0) {
            return false;
        }

        // 创建商品对象并生成唯一ID
        GoodsModel good = new GoodsModel();
        good.setGoodID(UUID.randomUUID().toString()); // 自动生成唯一ID
        good.setGoodName(goodName);
        good.setGoodSize(goodSize);
        good.setGoodPrice(goodPrice);
        good.setGoodNum(goodNum);

        // 调用DAO插入数据库
//        return goodsDao.insert(good);
        return goodsMapper.insert(good)>0;
    }

    /**
     * 添加商品（直接接收商品对象）
     */
    @Override
    @Transactional
    public GoodsModel addGood(GoodsModel good) {
        // 确保有ID（如果没有则生成）
        if (good.getGoodID() == null || good.getGoodID().isEmpty()) {
            good.setGoodID(UUID.randomUUID().toString());
        }
        if(goodsMapper.insert(good) > 0){
            redisTemplate.delete(detailKey(good.getGoodID()));
            return good;
        }else{
            return null;
        }

    }
    /**
     * 根据商品名称删除（实际应根据ID删除，名称可能不唯一）
     */
    @Override
    @Transactional
    public boolean deleteGoodByName(String goodName) {
        // 先根据名称查询（注意：可能查到多个，此处仅删除第一个）
        GoodsModel good = goodsDao.getByName(goodName);
        if (good != null) {
            return goodsDao.delete(good.getGoodID());
        }
        return false;
    }

    /**
     * 根据商品ID删除（顺便删除缓存中的商品缓存和库存缓存）
     */
    @Override
    @Transactional
    public boolean deleteGoodById(String id) {
        boolean ok = goodsMapper.deleteById(id) > 0;
        if(ok){
            redisTemplate.delete(detailKey(id));
            redisTemplate.delete(stockKey(id));
        }
        return ok;
    }

    /**
     * 根据名称模糊查询商品
     */
    @Override
    public GoodsModel getGoodByName(String query) {
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("查询条件不能为空");
        }
        return goodsMapper.selectByName(query); // 需确保DAO有模糊查询实现
    }

    /**
     * 根据商品ID查询
     */
    @Override
    public GoodsModel getGoodById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("商品ID不能为空");
        }
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        String key = detailKey(id);

        Object cached = ops.get(key);
        if (cached instanceof GoodsModel) {
            log.info("商品详情命中 Redis: {}", id);
            return (GoodsModel) cached;
        }
        GoodsModel good = goodsMapper.selectById(id);
        if (good != null) {
            ops.set(key, good, 30, TimeUnit.MINUTES);   // TTL 30 分钟
            log.info("商品详情回源 DB 并写入 Redis: {}", id);
        }
        return good;
    }

    /**
     * 获取所有商品
     */
    @Override
    public List<GoodsModel> getAllGood() {
        return goodsMapper.selectAll();
    }

    /**
     * 根据ID更新库存（增量）
     */
    @Override
    @Transactional
    public boolean updateStockById(String id, int quantity) {
        if (id == null || id.trim().isEmpty()) {
            log.warn("更新库存失败：商品ID为空");
            return false;
        }

        boolean updated;
        if (quantity < 0) {
            // 原子扣减，库存不足时受影响行数=0
            int changed = goodsMapper.deductStock(id, -quantity);
            updated = changed > 0;
            if (!updated) {
                log.warn("扣减库存失败：库存不足，id={}, amount={}", id, -quantity);
                return false;
            }
        } else if (quantity > 0) {
            updated = goodsMapper.increaseStock(id, quantity) > 0;
        } else {
            return true; // 改 0 当成功
        }

        if (updated) {
            // 失效两个相关缓存：库存&详情（你如果把 GoodModel 放缓存，这一步很关键）
            redisTemplate.delete("stock:" + id);
            redisTemplate.delete("goods:detail:" + id);
            log.info("库存更新成功，已清理缓存 [stock:{}}] 与 [goods:detail:{}]", id, id);
        }
        return updated;
    }


    /**
     * 更新商品信息（全量更新）顺手失效“详情 + 库存”两个缓存键
     */
    @Override
    @Transactional
    public boolean updateGood(GoodsModel goods) {
        if (goods.getGoodID() == null || goods.getGoodID().isEmpty()) {
            return false;
        }
        // 检查商品是否存在
        if (goodsDao.getById(goods.getGoodID()) == null) {
            return false;
        }
        boolean ok = goodsMapper.update(goods) > 0;
        if(ok){
            redisTemplate.delete(detailKey(goods.getGoodID()));
            redisTemplate.delete((stockKey(goods.getGoodID())));
        }
        return ok;
    }

    @Override
    public ResponseEntity<GoodStockDTO> getStockById(String goodId) {
        if (goodId == null || goodId.isBlank()) {
            log.warn("库存查询失败，输入的商品id不合规");
            return ResponseEntity.badRequest().build();
        }

        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        String key = detailKey(goodId);

        // 1) 先尝试从 Redis 拿整个 GoodModel
        long redisStart = System.nanoTime();
        Object obj = ops.get(key);
        long redisEnd = System.nanoTime();

        GoodsModel good;
        if (obj instanceof GoodsModel) {
            good = (GoodsModel) obj;
            log.info("详情命中 Redis，key={}，Redis 查询耗时: {} μs", key, (redisEnd - redisStart)/1_000);
        } else {
            // 2) 未命中才查 DB，并写回 Redis（给这个场景 TTL 可以短一些，比如 5 分钟）
            long dbStart = System.nanoTime();
            good = goodsMapper.selectById(goodId);
            long dbEnd = System.nanoTime();
            if (good == null) return ResponseEntity.notFound().build();

            ops.set(key, good, 5, TimeUnit.MINUTES); // 统一缓存 GoodModel
            log.info("详情来自 DB(耗时 {} ms)，已写入 Redis: {}", (dbEnd - dbStart)/1_000_000, key);
        }

        // 3) 组装返回（库存直接取对象中的 goodNum）
        GoodStockDTO dto = new GoodStockDTO();
        BeanUtils.copyProperties(good, dto);
        dto.setGoodNum(good.getGoodNum());
        return ResponseEntity.ok(dto);
    }

}