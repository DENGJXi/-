package com.project.dao;

import com.project.model.OrdersModel;

import javax.persistence.criteria.Order;
import java.time.LocalDateTime;
import java.util.List;

public interface OrdersDao {
    /**
     * 创建新订单
     * @param order 订单模型
     * @return
     */
    boolean create(OrdersModel order);

    /**
     * 根据订单ID获取订单详情（增）
     * @param orderId 订单ID
     * @return 订单模型（包含订单项）
     */
    OrdersModel getById(String orderId);

    /**
     * 根据时间段查询该时间段内的所有订单
     * @param startTime
     * @param endTime
     * @return 订单集合
     */
    List<OrdersModel> getOrderByTime(LocalDateTime startTime,LocalDateTime endTime);
    /**
     * 删除订单（逻辑删除）
     * @param orderId 订单ID
     * @return 是否删除成功
     */
    boolean delete(String orderId);

    /**
     * 返回数据库中的订单(查)
     * @return
     */
    List<OrdersModel> getAllOrders();
}