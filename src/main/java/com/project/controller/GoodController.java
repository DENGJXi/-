package com.project.controller;

import com.project.dao.GoodsDao;
import com.project.dto.good.GoodStockDTO;
import com.project.model.GoodsModel;
import com.project.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;
import java.util.List;
import java.util.Map;

// 使用 @RestController 替代 @Controller，默认返回 JSON 数据
@RestController
@RequestMapping("/goods")

public class GoodController {

    @Autowired
    private GoodService goodsService;

    // 1. 查询所有商品（基础查询）
    @GetMapping("/getAll")
    public ResponseEntity<List<GoodsModel>> listAll() {
        List<GoodsModel> goodsList = goodsService.getAllGood();
        // 返回 JSON 数据和 200 状态码
        return new ResponseEntity<>(goodsList, HttpStatus.OK);
    }

    // 2. 执行新增商品（创建）
    @PostMapping("/add")
    public ResponseEntity<GoodsModel> addGoods(@RequestBody GoodsModel goods) {
        goods.setGoodID(UUID.randomUUID().toString());
        GoodsModel savedGoods = goodsService.addGood(goods);
        // 返回完整实体 + 201状态码 + Location头
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedGoods.getGoodID())
                .toUri();

        return ResponseEntity.created(location).body(savedGoods);
    }

    // 3. 根据 ID 查询商品（用于修改表单回显，返回商品详情）
    @GetMapping("/edit/{id}")
    public ResponseEntity<GoodsModel> getGoodsById(@PathVariable String id) {
        GoodsModel goods = goodsService.getGoodById(id);
        if (goods != null) {
            // 找到商品，返回商品数据和 200 状态码
            return new ResponseEntity<>(goods, HttpStatus.OK);
        } else {
            // 未找到商品，返回 404 状态码
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 根据商品名称查询商品
     * @param name
     * @return
     */
    @GetMapping("/get/{name}")
    public ResponseEntity<GoodsModel> getGoodsByName(@PathVariable String name){
        GoodsModel goods = goodsService.getGoodByName(name);
        if (goods != null) {
            // 找到商品，返回商品数据和 200 状态码
            return new ResponseEntity<>(goods, HttpStatus.OK);
        } else {
            // 未找到商品，返回 404 状态码
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 查询商品库存
     * @param goodId
     * @return
     */
    @GetMapping("/getStock")
    public ResponseEntity<GoodStockDTO> getStockById(@RequestParam String goodId){
        return goodsService.getStockById(goodId);
    }

    // 3. 执行修改商品（更新）
    @PostMapping("/edit/{id}")
    public ResponseEntity<Map<String, String>> updateGoods(
            @PathVariable String id,
            @RequestParam String name,
            @RequestParam String spec,
            @RequestParam double price,
            @RequestParam int stock) {

        GoodsModel goods = new GoodsModel();
        goods.setGoodID(id); // 设置 ID 用于更新
        goods.setGoodName(name);
        goods.setGoodSize(spec);
        goods.setGoodPrice(price);
        goods.setGoodNum(stock);

        boolean success = goodsService.updateGood(goods); // 需实现 updateGood 方法
        if (success) {
            // 更新成功，返回 JSON 提示和 200 状态码
            return new ResponseEntity<>(
                    Map.of("message", "修改成功"),
                    HttpStatus.OK
            );
        } else {
            // 更新失败，返回 JSON 提示和 400 状态码
            return new ResponseEntity<>(
                    Map.of("error", "修改失败"),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    // 4. 删除商品
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGoods(@PathVariable String id) {
        boolean success = goodsService.deleteGoodById(id);
        if(success){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}