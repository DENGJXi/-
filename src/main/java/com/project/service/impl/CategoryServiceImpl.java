package com.project.service.impl;

import com.project.dto.CategoryRequestDTO;
import com.project.dto.CategoryResponseDTO;
import com.project.mapper.CategoryMapper;
import com.project.model.CategoryModel;
import com.project.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponseEntity<List<CategoryResponseDTO>> selectAll() {
        log.info("查询所有商品分类");
        List<CategoryModel> categories = categoryMapper.selectAll();
        if(categories == null || categories.isEmpty()){
            log.warn("未找到任何商品分类");
        }
        List<CategoryResponseDTO> dtos = categories.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<CategoryResponseDTO> addCategory(CategoryRequestDTO categoryDTO) {
       if(categoryDTO == null){
           log.warn("新建商品分类失败，新分类内容为空");
           return ResponseEntity.badRequest().build();
       }
       log.info("开始新建商品分类{}",categoryDTO);
        CategoryModel category = new CategoryModel();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        int result = categoryMapper.insert(category);
        if(result > 0){
            CategoryResponseDTO categoryResponseDTO = convertToResponseDTO(category);
            return ResponseEntity.ok(categoryResponseDTO);
        }
        log.error("新增商品分类失败：数据库操作未成功");
        return ResponseEntity.badRequest().build();

    }

    private CategoryResponseDTO convertToResponseDTO(CategoryModel category){
        CategoryResponseDTO dto = new CategoryResponseDTO();
        BeanUtils.copyProperties(category,dto);
        return dto;
    }
}
