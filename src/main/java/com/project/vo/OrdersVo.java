package com.project.vo;

import com.project.model.GoodsModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersVo {
    private String orderID;
    private String customerName;
    private String orderTimeStr; // 格式化后的时间字符串
    private String goodsName;
    private double goodsPrice;
    private int purchaseNum;
    private double totalPrice;
}
