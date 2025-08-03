package com.project.service;

import com.project.model.UsersModel;

public interface LoginService {
    /**
     * 查看是否存在该用户
     * @param userModel
     * @return
     */
    boolean checkLogin(UsersModel userModel);

    /**
     * 用户注册
     * @param user
     * @return
     */
    boolean register(UsersModel user);


}
