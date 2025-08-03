package com.project.dao;

import com.project.model.GoodsModel;
import java.util.List;

public interface GoodsDao {
    /**
     * 添加商品
     * @param good 商品对象
     * @return 是否添加成功
     */
    boolean insert(GoodsModel good);

    /**
     * 根据ID查询商品
     * @param id 商品ID
     * @return 商品对象，如果不存在返回null
     */
    GoodsModel getById(String id);

    /**
     * 根据名称查询商品
     * @param name 商品名称
     * @return 商品对象，如果不存在返回null
     */
    GoodsModel getByName(String name);

    /**
     * 获取所有商品列表
     * @return 商品列表
     */
    List<GoodsModel> getAll();

    /**
     * 更新商品信息
     * @param good 商品对象
     * @return 是否更新成功
     */
    boolean update(GoodsModel good);

    /**
     * 删除商品
     * @param id 商品ID
     * @return 是否删除成功
     */
    boolean delete(String id);

    /**
     * 检查商品是否存在
     * @param id 商品ID
     * @return 是否存在
     */
    boolean exists(String id);

    /**
     * 根据名称检查商品是否存在
     * @param name 商品名称
     * @return 是否存在
     */
    boolean existsByName(String name);

    /**
     * 更新商品库存
     * @param id 商品ID
     * @param quantity 变化数量（正数表示增加，负数表示减少）
     * @return 是否更新成功
     */
    boolean updateStock(String id, int quantity);
}