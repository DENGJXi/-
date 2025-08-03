package com.project.dto;

public class CreateOrderRequest {
    private String customerName;
    private String goodId;
    private int purchaseNum;

    // 无参构造函数
    public CreateOrderRequest() {}

    // 全参构造函数（可选）
    public CreateOrderRequest(String customerName, String goodId, int purchaseNum) {
        this.customerName = customerName;
        this.goodId = goodId;
        this.purchaseNum = purchaseNum;
    }

    // Getter 和 Setter 方法
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public int getPurchaseNum() {
        return purchaseNum;
    }

    public void setPurchaseNum(int purchaseNum) {
        this.purchaseNum = purchaseNum;
    }
}