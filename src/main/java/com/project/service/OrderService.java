package com.project.service;

import com.project.model.OrdersModel;
import com.project.vo.OrdersVo;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    /**
     * 创建新订单（增）
     * @param order
     * @return
     */
    boolean placeOrder(OrdersModel order);

    /**
     * 根据订单id删除订单（删）
     * @param id
     * @return
     */
    boolean deleteOrder(String id);
    /**
     * 根据订单id查找订单（查）
     * @param id
     * @return
     */
    OrdersModel getOrderById(String id);

    /**
     * 返回所有订单
     * @return
     */
    List<OrdersVo> getAll();
    /**
     * 根据开始时间和结束时间查询该时间段的订单（查）
     * @param startTime
     * @param endTime
     * @return
     */
    List<OrdersModel> getOrderByTime(LocalDateTime startTime,LocalDateTime endTime);
}
