package com.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 安全过滤链
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/category/**").permitAll()
                        .requestMatchers("/orders/**").permitAll()
                        .requestMatchers("/goods/**").permitAll()
                        .requestMatchers("/test/public").permitAll()
                        .requestMatchers("/api/product/public/**").permitAll()  // 公开接口
                        .requestMatchers("/api/product/add").hasRole("ADMIN")   // 仅 ADMIN 可访问
                        .requestMatchers("/test/admin", "/api/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                // 适配新版本：显式配置 HttpBasic
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}