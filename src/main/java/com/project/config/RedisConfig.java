package com.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    // 配置Redis连接工厂（使用RedisStandaloneConfiguration替代过时方法）
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // 1. 创建单机配置对象
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("localhost");  // Redis服务器地址
        config.setPort(6379);             // Redis端口号
        config.setPassword(RedisPassword.of(""));  // 密码（无密码则传空字符串）
        config.setDatabase(0);            // 选择数据库（默认0）

        // 2. 基于配置创建Lettuce连接工厂
        LettuceConnectionFactory factory = new LettuceConnectionFactory(config);

        // 3. 配置连接池（可选，根据需要添加）
        factory.setShareNativeConnection(false); // 不共享原生连接
        return factory;
    }

    // 配置RedisTemplate（序列化方式不变）
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 配置序列化器（避免存储乱码）
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();

        // 设置key的序列化方式
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // 设置value的序列化方式
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }
}


