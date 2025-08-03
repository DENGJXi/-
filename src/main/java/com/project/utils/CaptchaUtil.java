package com.project.utils;

import java.util.Random;

public class CaptchaUtil {
    private static final int CODE_LENGTH = 4;  // 验证码长度

    public static String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10)); // 0-9随机数
        }
        return code.toString();
    }
}
