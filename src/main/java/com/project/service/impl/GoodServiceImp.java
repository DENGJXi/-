package com.project.service.impl;

import com.project.dao.GoodsDao;
import com.project.dto.good.GoodStockDTO;
import com.project.mapper.GoodsMapper;
import com.project.model.GoodsModel;
import com.project.service.GoodService;
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
public class GoodServiceImp implements GoodService {

    @Autowired
    private GoodsDao goodsDao; // 使用接口而非实现类
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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
     * 根据商品ID删除
     */
    @Override
    @Transactional
    public boolean deleteGoodById(String id) {
        return goodsMapper.deleteById(id)>0;
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
        return goodsMapper.selectById(id);
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
        // 检查库存是否足够（如果是减少库存）
        if (quantity < 0) {
            GoodsModel good = goodsDao.getById(id);
            if (good == null || good.getGoodNum() < Math.abs(quantity)) {
                return false; // 库存不足
            }
        }
        // 1. 更新数据库库存
        boolean updated = goodsMapper.updateStock(id, quantity) > 0;

        // 2. 清理 Redis 缓存（只有更新成功才清）
        if (updated) {
            String redisKey = "stock:" + id;
            redisTemplate.delete(redisKey);
            log.info("库存更新成功，已清理缓存 [{}]", redisKey);
        } else {
            log.warn("库存更新失败：数据库未受影响");
        }
        return updated;
    }

    /**
     * 更新商品信息（全量更新）
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
        return goodsMapper.update(goods) > 0;
    }

    @Override
    public ResponseEntity<GoodStockDTO> getStockById(String goodId) {
        if(goodId == null ){
            log.warn("库存查询失败，输入的商品id不合规");
            return ResponseEntity.badRequest().build();
        }
        String redisKey = "stock"+goodId;
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();

        long dbStart = System.nanoTime(); // ⏱️ start DB timing
        GoodsModel good = goodsMapper.selectById(goodId);
        long dbEnd = System.nanoTime();   // ⏱️ end DB timing
        if(good == null){
            log.warn("未找到该商品");
            return ResponseEntity.notFound().build();
        }

        //1.尝试读取缓存
        long redisStart = System.nanoTime(); // ⏱️ start Redis timing
        Object cached = ops.get(redisKey);
        long redisEnd = System.nanoTime();   // ⏱️ end Redis timing
        int stock;
        if (cached != null) {
            stock = Integer.parseInt(cached.toString());
            log.info("库存来自 Redis 缓存");
        } else {
            stock = good.getGoodNum();
            // 写入 Redis，5 分钟过期
            ops.set(redisKey, stock, 5, TimeUnit.MINUTES);
            log.info("库存来自数据库，并已写入缓存");
        }

        GoodStockDTO dto = new GoodStockDTO();
        dto.setGoodId(good.getGoodID());
        dto.setGoodName(good.getGoodName());
        dto.setGoodSize(good.getGoodSize());
        dto.setGoodPrice(good.getGoodPrice());
        dto.setGoodNum(stock); // 用缓存/数据库库存

        // 打印计时结果（毫秒换算）
        log.info("数据库查询耗时: {} ms", (dbEnd - dbStart) / 1_000_000);
        log.info("Redis 查询耗时: {} μs", (redisEnd - redisStart) / 1_000); // 微秒

        return ResponseEntity.ok(dto);
    }
}