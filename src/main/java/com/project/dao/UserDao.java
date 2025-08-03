package com.project.dao;

import com.project.model.UsersModel;
import java.util.List;

public interface UserDao {
    // 添加用户
    boolean insert(UsersModel user);

    // 根据用户名查询用户
    UsersModel getByUsername(String username);

    // 获取所有用户
    List<UsersModel> getAll();

    // 更新用户信息
    boolean update(UsersModel user);

    // 删除用户
    boolean delete(String username);

    // 检查用户是否存在
    boolean exists(String username);
}