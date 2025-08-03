package com.project.utils;
import org.springframework.stereotype.Service;

import java.util.regex.*;

@Service
public class ValidateUtil {
    /**
     * 正则验证用户名格式
     * @param user 要验证的用户名
     * @return boolean
     */
    public static boolean checkUser(String user){
        return Pattern.compile("^[a-zA-Z0-9_]+$").matcher(user).matches();
    }
    /**
     * 正则验证密码格式
     * @param pwd 要验证的密码
     * @return boolean
     */
    public static boolean checkPassword(String pwd){
        return Pattern.compile("^.{6,10}$").matcher(pwd).matches();
    }
}
