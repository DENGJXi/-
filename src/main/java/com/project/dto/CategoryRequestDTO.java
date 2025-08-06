package com.project.dto;

import lombok.Data;

@Data
public class CategoryRequestDTO {
    private String name;
    private Long parentId;
    private Integer status;
    private Integer sort;
}
