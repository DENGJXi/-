package com.project.controller;

import com.project.dto.CategoryRequestDTO;
import com.project.dto.CategoryResponseDTO;
import com.project.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有商品种类
     * @return
     */
    @GetMapping("/selectAll")
    public ResponseEntity<List<CategoryResponseDTO>> selectAll(){
        return categoryService.selectAll();
    }

    /**
     * 新增商品种类
     * @param category
     * @return
     */
    @PostMapping("/addCategory")
    public ResponseEntity<CategoryResponseDTO> addCategory(@RequestBody CategoryRequestDTO category){
        return categoryService.addCategory(category);
    }


}
