package com.project.dto.category;

import lombok.Data;

@Data
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private Long parentId;
    private Integer status;
    private Integer sort;
}
