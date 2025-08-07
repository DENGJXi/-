package com.project.mapper;

import com.project.model.CategoryModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    // 查询所有分类
    List<CategoryModel> selectAll();

    // 根据ID查询
    CategoryModel selectById(Long id);

    // 添加分类
    int insert(CategoryModel category);

    // 修改分类
    int update(CategoryModel category);

    // 删除分类
    int deleteById(Long id);


}

