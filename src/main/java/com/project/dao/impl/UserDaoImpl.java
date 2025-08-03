package com.project.dao.impl;

import com.project.dao.UserDao;
import com.project.model.UsersModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository // 标注为 Spring 数据访问层组件，让 Spring 管理其生命周期
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate ; // 注入 Spring 提供的 JdbcTemplate

    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    // 定义 RowMapper，用于将 ResultSet 中的数据映射为 UsersModel 对象
    private static final RowMapper<UsersModel> userRowMapper = new RowMapper<UsersModel>() {
        @Override
        public UsersModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new UsersModel(rs.getString("user_name"),rs.getString("user_password"));
        }
    };

    /**
     * 用户注册（增）
     * @param user
     */
    @Override
    public boolean insert(UsersModel user) {
        String sql = "INSERT INTO users(user_name, user_password) VALUES(?, ?)";
        try {
            log.info("执行SQL: {}", sql);
            log.info("参数: userName={}, userPassword=*****", user.getUserName());
            int rowsAffected = jdbcTemplate.update(sql, user.getUserName(), user.getUserPassword());
            return rowsAffected > 0;
        } catch (DataAccessException e) {
            // 打印完整的SQL异常信息
            log.error("SQL执行失败: {}", e.getMessage(), e);
            return false;
        }
    }
    /**
     * 注销用户（删）
     * @param username
     */
    @Override
    public boolean delete(String username) {
        String sql = "DELETE FROM users WHERE user_name = ?";
        int affectedRows = jdbcTemplate.update(sql, username);
        return affectedRows > 0;
    }
    /**
     * 更新用户数据（改）
     * @param user
     */
    @Override
    public boolean update(UsersModel user) {
        String sql = "UPDATE users SET user_password = ? WHERE user_name = ?";
        int affectedRows = jdbcTemplate.update(sql, user.getUserPassword(), user.getUserName());
        return affectedRows > 0;
    }
    /**
     * 根据用户名查找用户（查）
     * @param username
     * @return
     */
    @Override
    public UsersModel getByUsername(String username) {
        String sql = "SELECT * FROM users WHERE user_name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, username);
        } catch (Exception e) {
            // 查询结果为空时会抛出异常，这里捕获并返回 null
            return null;
        }
    }
    /**
     * 返回所有用户数据（查）
     * @return
     */
    @Override
    public List<UsersModel> getAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }
    /**
     * 判断该用户是否存在（查）
     * @param username
     * @return
     */
    @Override
    public boolean exists(String username) {
        log.info("检查用户名是否存在: {}", username);
        String sql = "SELECT COUNT(*) FROM users WHERE user_name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        log.info("查询结果: {}", count);
        return count != null && count > 0;
    }
}