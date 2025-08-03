package com.project.dao.impl;

import com.project.dao.GoodsDao;
import com.project.model.GoodsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GoodsDaoImpl implements GoodsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 商品对象映射器：将数据库结果集映射为 GoodsModel 对象
    private static final RowMapper<GoodsModel> goodsRowMapper = new RowMapper<GoodsModel>() {
        public GoodsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new GoodsModel(
                    rs.getString("good_id"),
                    rs.getString("good_name"),
                    rs.getString("good_size"),
                    rs.getDouble("good_price"),
                    rs.getInt("good_num")
            );
        }
    };

    // =============== 增 ===============

    /**
     * 新增商品记录
     * @param good 商品实体
     * @return 操作成功返回 true，失败返回 false
     */
    @Override
    public boolean insert(GoodsModel good) {
        String sql = "INSERT INTO goods(good_id, good_name, good_size, good_price, good_num) VALUES(?,?,?,?,?)";
        int affectedRows = jdbcTemplate.update(sql,
                good.getGoodID(),
                good.getGoodName(),
                good.getGoodSize(),
                good.getGoodPrice(),
                good.getGoodNum());
        return affectedRows > 0;
    }

    // =============== 删 ===============

    /**
     * 根据商品ID删除记录
     * @param id 商品ID
     * @return 操作成功返回 true，失败返回 false
     */
    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM goods WHERE good_id = ?";
        int affectedRows = jdbcTemplate.update(sql, id);
        return affectedRows > 0;
    }

    // =============== 改 ===============

    /**
     * 更新商品信息（全量更新）
     * @param good 商品实体（需包含ID）
     * @return 操作成功返回 true，失败返回 false
     */
    @Override
    public boolean update(GoodsModel good) {
        String sql = "UPDATE goods SET good_name=?, good_size=?, good_price=?, good_num=? WHERE good_id=?";
        int affectedRows = jdbcTemplate.update(sql,
                good.getGoodName(),
                good.getGoodSize(),
                good.getGoodPrice(),
                good.getGoodNum(),
                good.getGoodID());
        return affectedRows > 0;
    }

    /**
     * 调整商品库存（增量更新）
     * @param id 商品ID
     * @param quantity 库存变化量（正数表示增加，负数表示减少）
     * @return 操作成功返回 true，失败返回 false
     */
    @Override
    public boolean updateStock(String id, int quantity) {
        String sql = "UPDATE goods SET good_num = good_num + ? WHERE good_id=?";
        int affectedRows = jdbcTemplate.update(sql, quantity, id);
        return affectedRows > 0;
    }

    // =============== 查 ===============

    /**
     * 根据商品ID查询单个商品
     * @param id 商品ID
     * @return 存在则返回商品实体，不存在则返回 null
     */
    @Override
    public GoodsModel getById(String id) {
        String sql = "SELECT * FROM goods WHERE good_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, goodsRowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据商品名称查询单个商品（用于唯一名称校验）
     * @param name 商品名称
     * @return 存在则返回商品实体，不存在则返回 null
     */
    @Override
    public GoodsModel getByName(String name) {
        String sql = "SELECT * FROM goods WHERE good_name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, goodsRowMapper, name);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 查询所有商品列表
     * @return 商品列表（若为空则返回空集合）
     */
    @Override
    public List<GoodsModel> getAll() {
        String sql = "SELECT * FROM goods";
        return jdbcTemplate.query(sql, goodsRowMapper);
    }

    /**
     * 判断商品ID是否存在
     * @param id 商品ID
     * @return 存在返回 true，不存在返回 false
     */
    @Override
    public boolean exists(String id) {
        String sql = "SELECT COUNT(*) FROM goods WHERE good_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    /**
     * 判断商品名称是否存在
     * @param name 商品名称
     * @return 存在返回 true，不存在返回 false
     */
    @Override
    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM goods WHERE good_name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }
}