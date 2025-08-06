package com.project.model;

import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;
@Data
public class CategoryModel {
    //分类id
    private Long id;
    //分类名称
    private String name;
    //父类id
    private Long parentId;
    //分类状态 0表示下架 1表示上架
    private Integer status;
    //控制分类显示顺序
    private Integer sort;
    //创建时间
    private LocalDateTime createTime;
    //修改时间
    private LocalDateTime updateTime;
}
