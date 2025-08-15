package com.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
@MapperScan("com.project.mapper") // 精确到 Mapper 接口所在包
public class ProjectV2Application {
    public static void main(String[] args) {
        SpringApplication.run(ProjectV2Application.class, args);
    }
}