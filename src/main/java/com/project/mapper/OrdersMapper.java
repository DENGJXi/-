package com.project.mapper;

import com.project.model.OrdersModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrdersMapper {
    /**
     * 创建新订单
     *
     * @param order 订单模型
     * @return
     */
    int create(OrdersModel order);

    /**
     * 根据订单ID获取订单详情（增）
     *
     * @param orderId 订单ID
     * @return 订单模型（包含订单项）
     */
    OrdersModel getById(String orderId);

    /**
     * 分页查询订单
     *
     * @param offset   偏移量
     * @param pageSize 每页大小
     * @return 订单集合
     */
    List<OrdersModel> getOrdersByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 根据时间段查询该时间段内的所有订单
     *
     * @param startTime
     * @param endTime
     * @return 订单集合
     */
    List<OrdersModel> getOrderByTime(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 删除订单（逻辑删除）
     *
     * @param orderId 订单ID
     * @return 是否删除成功
     */
    int delete(String orderId);

    /**
     * 返回数据库中的订单(查)
     *
     * @return
     */
    List<OrdersModel> getAllOrders();

}
