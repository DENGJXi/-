package com.project.service.impl;

import com.project.dao.UserDao;
import com.project.model.UsersModel;
import com.project.service.LoginService;
import com.project.utils.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImp implements LoginService {
    @Autowired
    private ValidateUtil validateUtil ;

    @Autowired
    private UserDao userDao;
    /**
     * 查看是否存在该用户
     * @param userModel
     * @return
     */
    @Override
    public boolean checkLogin(UsersModel userModel){
        UsersModel existingUser = userDao.getByUsername(userModel.getUserName());
        if (existingUser == null) return false;
        return existingUser!=null && existingUser.getUserPassword().equals(userModel.getUserPassword());
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    public boolean register(UsersModel user){
        if (user.getUserName() == null || user.getUserName().trim().isEmpty()
                || user.getUserPassword() == null || user.getUserPassword().trim().isEmpty()) {
            return false;
        }
        if(userDao.exists(user.getUserName())){
            return false;
        }
        if(!validateUtil.checkUser(user.getUserName())||!validateUtil.checkPassword(user.getUserPassword())){
            return false;
        }
        return userDao.insert(user);
    }
}
