package com.project.controller;

import com.project.dto.CreateOrderRequest;
import com.project.model.GoodsModel;
import com.project.model.OrdersModel;
import com.project.service.GoodService;
import com.project.service.OrderService;
import java.util.UUID;

import com.project.vo.OrdersVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodService goodService;

    @GetMapping("/getAll")
    public ResponseEntity<List<OrdersVo>> listAll() {
        List<OrdersVo> ordersList = orderService.getAll();
        return new ResponseEntity<>(ordersList, HttpStatus.OK);
    }

    @GetMapping("/getById")
    public ResponseEntity<OrdersModel> getOrderById(@PathVariable String id) {
        OrdersModel order = orderService.getOrderById(id);
        if (order != null) {
            // 找到商品，返回商品数据和 200 状态码
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            // 未找到商品，返回 404 状态码
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getByTime")
    public ResponseEntity<List<OrdersModel>> getOrderByTime(
            @RequestParam String startTime,  // 接收日期字符串（如 "2023-10-01"）
            @RequestParam String endTime) {
        // 补全时分秒：startTime 设为 00:00:00，endTime 设为 23:59:59
        LocalDateTime start = LocalDate.parse(startTime).atStartOfDay();  // 当天0点
        LocalDateTime end = LocalDate.parse(endTime).atTime(23, 59, 59);  // 当天23点59分59秒

        List<OrdersModel> ordersList = orderService.getOrderByTime(start, end);
        return ResponseEntity.ok(ordersList);
    }

    @PostMapping("/createOrder")
    public ResponseEntity<OrdersModel> createOrder(@RequestBody CreateOrderRequest request) {
        //查找商品是否存在
        try {
            GoodsModel good = goodService.getGoodById(request.getGoodId());
            if (good == null) {
                return ResponseEntity.notFound().build();
            }
            OrdersModel order = new OrdersModel();
            order.setOrderTime(LocalDateTime.now());
            order.setOrderID(UUID.randomUUID().toString());
            order.setCustomerName(request.getCustomerName());
            order.setGood(good);
            order.setPurchaseNum(request.getPurchaseNum());
            order.setTotalPrice(request.getPurchaseNum() * good.getGoodPrice());

            boolean success = orderService.createOrder(order);
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED).body(order);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteOrder(@RequestParam String orderId) {
        boolean isDeleted = orderService.deleteOrder(orderId);
        if (isDeleted) {
            // 删除成功：返回 204 No Content（无内容，标准做法）
            return ResponseEntity.noContent().build();
        } else {
            // 删除失败（如订单不存在）：返回 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }
}
