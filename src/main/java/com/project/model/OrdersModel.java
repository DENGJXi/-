package com.project.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrdersModel {
    protected LocalDateTime orderTime;
    protected String orderID;
    protected String customerName;
    protected GoodsModel good;
    protected int purchaseNum;
    protected double totalPrice;

    // 无参构造函数（必须有！用于 JSON 反序列化）
    public OrdersModel() {
    }

    // 只保留必要的构造函数
    public OrdersModel(String customerName, GoodsModel good, int purchaseNum) {
        this.orderTime = LocalDateTime.now();
        this.orderID = UUID.randomUUID().toString();
        this.customerName = customerName;
        this.good = good;
        this.purchaseNum = purchaseNum;
        this.totalPrice = purchaseNum * good.getGoodPrice();
    }

    // 另一个有参构造函数（可选）
    public OrdersModel(LocalDateTime orderTime, String orderID, String customerName,
                       String goodID, String goodName, String goodSize, double goodPrice,
                       int purchaseNum, double totalPrice) {
        this.orderTime = orderTime;
        this.orderID = orderID;
        this.customerName = customerName;
        this.good = new GoodsModel(goodID, goodName, goodSize, goodPrice, 0);
        this.purchaseNum = purchaseNum;
        this.totalPrice = totalPrice;
    }

    // 计算总价（优化版）
    private double calculatePrice() {
        if (good == null || good.getGoodPrice() <= 0) {
            return 0;
        }
        return purchaseNum * good.getGoodPrice();
    }

    // Getter 和 Setter（优化总价计算逻辑）
    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public GoodsModel getGood() {
        return good;
    }

    public void setGood(GoodsModel good) {
        this.good = good;
        if (purchaseNum > 0) {
            this.totalPrice = calculatePrice();
        }
    }

    public int getPurchaseNum() {
        return purchaseNum;
    }

    public void setPurchaseNum(int purchaseNum) {
        this.purchaseNum = purchaseNum;
        if (good != null) {
            this.totalPrice = calculatePrice();
        }
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}