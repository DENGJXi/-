package com.project.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.project.dao.OrdersDao;
import com.project.model.OrdersModel;
import com.project.service.OrderService;
import com.project.vo.OrdersVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImp implements OrderService {
    @Autowired
    private OrdersDao ordersDao;

    @Override
    public boolean createOrder(OrdersModel order) {
        return ordersDao.create(order);
    }

    @Override
    public boolean deleteOrder(String id) {
        return ordersDao.delete(id);
    }

    @Override
    public List<OrdersVo> getAll() {
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //读取数据库中所有订单
        List<OrdersModel> orders = ordersDao.getAllOrders();
        //初始化ordersVo列表表用于存储拷贝后的数据
        List<OrdersVo> ordersVo = new ArrayList<>();
        //遍历订单，进行vo拷贝
        for(OrdersModel order:orders){
            //初始化orders的vo
            OrdersVo vo = new OrdersVo();
            BeanUtils.copyProperties(order,vo);
            if(order.getGood()!=null){
                vo.setGoodsName(order.getGood().getGoodName());
                vo.setGoodsPrice(order.getGood().getGoodPrice());
            }
            if(order.getOrderTime()!=null){
                vo.setOrderTimeStr(order.getOrderTime().format(formatter));
            }
            ordersVo.add(vo);
        }
        return ordersVo;
    }
    @Override
    public OrdersModel getOrderById(String id) {
        return ordersDao.getById(id);
    }

    @Override
    public List<OrdersModel> getOrderByTime(LocalDateTime startTime, LocalDateTime endTime) {
        return ordersDao.getOrderByTime(startTime,endTime);
    }
}
