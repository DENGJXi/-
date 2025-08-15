package com.project.filter;

import com.project.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public JwtAuthFilter(JwtUtil jwtUtil, RedisTemplate<String, String> redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    // 简单白名单
    private static final List<String> WHITELIST = List.of(
            "/api/auth/login",
            "/api/auth/captcha",
            "/test/public",
            "/api/product/public"
    );

    private boolean isWhitelisted(String uri) {
        return WHITELIST.stream().anyMatch(uri::equals);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        if (isWhitelisted(uri)) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            unauthorized(response, "缺少 Authorization 头");
            return;
        }

        String token = header.substring(7);
        try {
            Claims claims = jwtUtil.parseToken(token);
            String username = claims.getSubject();

            String redisKey = "TOKEN:" + token; // 与登录时存的一致
            if (!Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
                unauthorized(response, "token 未登记或已失效");
                return;
            }

            // 给后续控制器拿
            request.setAttribute("username", username);
            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            unauthorized(response, "token 过期");
        } catch (JwtException e) {
            unauthorized(response, "token 非法");
        }
    }

    private void unauthorized(HttpServletResponse resp, String msg) throws IOException {
        resp.setStatus(HttpStatus.UNAUTHORIZED.value());
        resp.setContentType("text/plain;charset=UTF-8");
        resp.getWriter().write(msg);
    }
}

