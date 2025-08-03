package com.project.dao.impl;

import com.project.dao.OrdersDao;
import com.project.model.OrdersModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrdersDaoImpl implements OrdersDao {
    //订单对象映射器：将数据库结果集映射为 OrdersModel 对象
    private final static RowMapper<OrdersModel> ordersRowMapper = new RowMapper<OrdersModel>() {
        @Override
        public OrdersModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new OrdersModel(rs.getObject("order_time", LocalDateTime.class),
                    rs.getString("order_id"),
                    rs.getString("customer_name"),
                    rs.getString("good_id"),
                    rs.getString("good_name"),
                    rs.getString("good_size"),
                    rs.getDouble("good_price"),
                    rs.getInt("purchase_num"),
                    rs.getDouble("total_price"));
        }
    };
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 插入订单（增）
     * @param order 订单模型
     * @return
     */
    @Override
    public boolean create(OrdersModel order) {
       String sql = "INSERT INTO orders(order_time,order_id,customer_name,good_id,good_name,good_size,good_price,purchase_num,total_price) " +
               "VALUES(?,?,?,?,?,?,?,?,?)";
       int affectedRows = jdbcTemplate.update(sql,
               order.getOrderTime(),
               order.getOrderID(),
               order.getCustomerName(),
               order.getGood().getGoodID(),
               order.getGood().getGoodName(),
               order.getGood().getGoodSize(),
               order.getGood().getGoodPrice(),
               order.getPurchaseNum(),
               order.getTotalPrice()
               );
        return affectedRows > 0 ;
    }

    /**
     * 根据订单id查找订单（查）
     * @param orderId 订单ID
     * @return
     */
    @Override
    public OrdersModel getById(String orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        try{
            //用于查询单条数据，并将结果映射为对象
            return jdbcTemplate.queryForObject(sql,ordersRowMapper,orderId);
        }catch (Exception e){
            return null;
        }

    }

    /**
     * 根据时间段查询该时间段内的所有订单
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<OrdersModel> getOrderByTime(LocalDateTime startTime, LocalDateTime endTime) {
        String sql = "SELECT * FROM orders WHERE  order_time >= ? AND order_time <= ?";
        return jdbcTemplate.query(sql,ordersRowMapper,startTime,endTime);
    }

    /**
     * 根据订单id删除订单
     * @param orderId 订单ID
     * @return
     */
    @Override
    public boolean delete(String orderId) {
        String sql = "DELETE  FROM orders WHERE order_id = ?";
        int affectedRows = jdbcTemplate.update(sql,orderId);
        return affectedRows > 0;
    }

    /**
     * 返回数据库中的所有订单
     * @return
     */
    @Override
    public List<OrdersModel> getAllOrders() {
        String sql = "SELECT * FROM orders";
        //用query返回查询到的所有数据
        return jdbcTemplate.query(sql, ordersRowMapper);
    }
}
