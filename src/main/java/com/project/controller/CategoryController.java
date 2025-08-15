package com.project.controller;

import com.project.dto.category.CategoryRequestDTO;
import com.project.dto.category.CategoryResponseDTO;
import com.project.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 根据分类id查找分类
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> selectCategoryById(@PathVariable Long id){
        return categoryService.selectById(id);
    }
    /**
     * 查询所有商品种类
     * @return
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> selectAll(){
        return categoryService.selectAll();
    }

    /**
     * 新增商品种类
     * @param category
     * @return
     */
    @PostMapping//@PostMapping：用于创建新资源
    public ResponseEntity<CategoryResponseDTO> addCategory(@RequestBody CategoryRequestDTO category){
        return categoryService.addCategory(category);
    }

    /**
     * 删除商品分类
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@RequestParam Long id){
        return categoryService.deleteCategoryById(id);
    }

    /**
     * 更新商品分类信息
     * @param category
     * @return
     */
    @PutMapping("/{id}") //@PutMapping：用于更新已有资源
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryRequestDTO category){
        return categoryService.updateCategory(id,category);
    }
}
