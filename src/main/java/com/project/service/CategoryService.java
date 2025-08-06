package com.project.service;

import com.project.dto.CategoryRequestDTO;
import com.project.dto.CategoryResponseDTO;
import com.project.model.CategoryModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    /**
     * 查询所有商品种类
     * @return
     */
    ResponseEntity<List<CategoryResponseDTO>> selectAll();

    /**
     * 新增商品种类
     * @param category
     * @return
     */
    ResponseEntity<CategoryResponseDTO> addCategory(CategoryRequestDTO category);

    /**
     * 删除商品种类
     * @param id
     * @return
     */
    ResponseEntity<Void> deleteCategoryById(Long id);

    /**
     * 根据分类id查找分类
     * @param id
     * @return
     */
    ResponseEntity<CategoryResponseDTO> selectById(Long id);

    /**
     * 更新商品分类信息
     * @param category
     * @return
     */
    ResponseEntity<CategoryResponseDTO> updateCategory(Long id,CategoryRequestDTO category);
}
