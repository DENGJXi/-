package com.project.dto.good;

import lombok.Data;

@Data
public class GoodStockDTO {
    private String goodId;
    private String goodName;
    private String goodSize;
    private double goodPrice;
    private int goodNum;
}
