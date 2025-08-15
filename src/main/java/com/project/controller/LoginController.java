package com.project.controller;

import com.project.model.UsersModel;
import com.project.service.LoginService;
import com.project.utils.CaptchaUtil;
import com.project.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 1. 获取验证码（通过 HttpServletRequest 获取 Session）
    @GetMapping(value = "/captcha", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> getCaptcha(HttpServletRequest request) {
        // 通过 request 获取 HttpSession（若不存在则创建）
        HttpSession session = request.getSession();
        String code = CaptchaUtil.generateCode();
        session.setAttribute("captchaCode", code); // 存储验证码
        Map<String, String> result = new HashMap<>();
        result.put("code", code);
        return ResponseEntity.ok(result);
    }

    // 2. 登录接口（校验验证码）
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody UsersModel user,
            HttpServletRequest request
    ) {
//        HttpSession session = request.getSession();
//        String savedCode = (String) session.getAttribute("captchaCode");

        // 验证码校验
//        if (savedCode == null || !savedCode.equalsIgnoreCase(captcha)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("msg", "验证码错误"));
//        }

//        // 移除验证码，防止重复使用
//        session.removeAttribute("captchaCode");

        // 校验用户名密码
        boolean success = loginService.checkLogin(user);
        if (!success) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("msg", "用户名或密码错误"));
        }
        // ✅ 登录成功，生成 JWT Token
        String token = jwtUtil.generateToken(user.getUserName());

        // 存入 Redis（键名要和过滤器里校验的一致）
        String redisKey = "TOKEN:" + token; // 你的过滤器当前就是按 token 查的
        redisTemplate.opsForValue().set(redisKey, "1", Duration.ofHours(1));

        // 返回 token 给前端
        return ResponseEntity.ok(Map.of(
                "msg", "登录成功",
                "token", token
        ));
    }

    /**
     * 登出接口
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token)) {
            redisTemplate.delete("TOKEN:" + token);
        }
        return ResponseEntity.ok("退出成功");
    }

    // 3. 注册接口
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UsersModel user) {
        System.out.println("接收到的用户：" + user);
        boolean success = loginService.register(user);
        return success ?
                ResponseEntity.ok("注册成功") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("注册失败");
    }
}