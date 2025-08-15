package com.project.mapper;

import com.project.model.GoodsModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsMapper {

    int insert(GoodsModel goods);            // ✅ XML 中也是 insert
    int deleteById(String id);               // ❗ 原来是 delete → 改成 deleteById
    int update(GoodsModel good);             // ✅ 一致
    int increaseStock(@Param("id") String id, @Param("quantity") int quantity); // ✅
    int deductStock(@Param("id") String id, @Param("quantity") int quantity);

    GoodsModel selectById(String id);        // ❗ getById → 改成 selectById
    GoodsModel selectByName(String name);    // ❗ getByName → 改成 selectByName
    List<GoodsModel> selectAll();            // ❗ getAll → 改成 selectAll

    int countById(String id);                // ✅
    int countByName(String name);            // ✅

    String getNameById(String id);
}
