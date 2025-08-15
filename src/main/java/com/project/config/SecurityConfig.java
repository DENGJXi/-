//package com.project.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    // 安全过滤链
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/captcha").permitAll()  // ✅ 单独放行
//                        .requestMatchers("/api/auth/login").permitAll()    // ✅ 登录也要放行
//                        .requestMatchers("/category/**").permitAll()
//                        .requestMatchers("/orders/**").permitAll()
//                        .requestMatchers("/goods/**").permitAll()
//                        .requestMatchers("/test/public").permitAll()
//                        .requestMatchers("/api/product/public/**").permitAll()
//                        .requestMatchers("/api/product/add").hasRole("ADMIN")
//                        .requestMatchers("/test/admin", "/api/admin/**").hasRole("ADMIN")
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(Customizer.withDefaults());
//
//        return http.build();
//    }
//
//}