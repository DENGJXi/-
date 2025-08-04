package com.project.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.project.dao.OrdersDao;
import com.project.mapper.GoodsMapper;
import com.project.mapper.OrdersMapper;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImp.class);

    @Override
    public boolean placeOrder(OrdersModel order) {
        if(order.getPurchaseNum() > goodsMapper.getStock(order.getGood().getGoodID())){
            throw new RuntimeException("库存不足");
        }
        goodsMapper.updateStock(order.getGood().getGoodID(),-order.getPurchaseNum());
        return ordersMapper.create(order) > 0;
    }

    @Override
    public boolean deleteOrder(String id) {
        return ordersMapper.delete(id) > 0;
    }

    @Override
    public List<OrdersVo> getAll() {
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //读取数据库中所有订单
        List<OrdersModel> orders = ordersMapper.getAllOrders();
        //初始化ordersVo列表表用于存储拷贝后的数据
        List<OrdersVo> ordersVo = new ArrayList<>();
        //遍历订单，进行vo拷贝
        for(OrdersModel order:orders){
            log.debug("订单ID: {}", order.getOrderID());
            log.debug("商品对象: {}", order.getGood());
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
        return ordersMapper.getById(id);
    }

    @Override
    public List<OrdersModel> getOrderByTime(LocalDateTime startTime, LocalDateTime endTime) {
        return ordersMapper.getOrderByTime(startTime,endTime);
    }
}
