package com.project.model;
import java.util.UUID;
public class GoodsModel {
    protected String goodID;
    protected String goodName;
    protected String goodSize;
    protected double goodPrice;
    //商品库存
    protected int goodNum;

    public GoodsModel() {
    }

    public GoodsModel(String goodName, String goodSize, double goodPrice, int goodNum) {
        this.goodID = UUID.randomUUID().toString();
        this.goodName = goodName;
        this.goodSize = goodSize;
        this.goodPrice = goodPrice;
        this.goodNum = goodNum;
    }

    public GoodsModel(String goodID, String goodName, String goodSize, double goodPrice, int goodNum) {
        this.goodID = goodID;
        this.goodName = goodName;
        this.goodSize = goodSize;
        this.goodPrice = goodPrice;
        this.goodNum = goodNum;
    }

    public String getGoodID() {
        return goodID;
    }

    public void setGoodID(String goodID) {
        this.goodID = goodID;
    }

    public String getGoodName(){
        return goodName;
    }

    public void setGoodName(String goodName){
        this.goodName = goodName;
    }

    public String getGoodSize(){
        return goodSize;
    }

    public void setGoodSize(String goodSize){
        this.goodSize = goodSize;
    }

    public int getGoodNum(){
        return goodNum;
    }

    public void setGoodNum(int goodNum){
        this.goodNum = goodNum;
    }

    public double getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(double goodPrice) {
        this.goodPrice = goodPrice;
    }

}
