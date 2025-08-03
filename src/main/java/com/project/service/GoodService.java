package com.project.service;

//import com.project.mapper.GoodsMapper;
import com.project.model.GoodsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

public interface GoodService {

    /**
     * 展示所有商品
     * @return
     */
    List<GoodsModel> getAllGood();
    /**
     * 商品保存功能，将用户输入的商品保存在（增）
     * @param goodName
     * @param goodSize
     * @param goodPrice
     * @param goodNum
     * @return
     */
    boolean addGood(String goodName, String goodSize, double goodPrice, int goodNum);
    /**
     * 商品保存功能，将用户输入的商品保存在（增）
     * @param good
     * @return
     */
    GoodsModel addGood(GoodsModel good);
    /**
     * 根据商品名称对商品进行移除（删）
     * @param goodName
     */
    boolean deleteGoodByName(String goodName);
    /**
     * 根据商品id删除商品
     * @param id
     * @return
     */
    boolean deleteGoodById(String id);
    /**
     * 输入商品的部分或完整名称进行查询（查）
     * @param query
     */
    GoodsModel getGoodByName(String query);
    /**
     * 根据商品id查询商品（查）
     * @param id
     * @return
     */
    GoodsModel getGoodById(String id);
    /**
     * 根据商品id编辑库存（改）
     * @param id
     * @param num
     * @return
     */
    boolean updateStockById(String id, int num);

    /**
     * 更新商品数据
     * @param goods
     * @return
     */
    boolean updateGood(GoodsModel goods);
}