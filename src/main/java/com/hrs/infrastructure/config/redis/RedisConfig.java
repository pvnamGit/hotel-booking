//package com.hrs.infrastructure.config.redis;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//public class RedisConfig {
//  @Value("${redis.host}")
//  private String redisHost;
//
//  @Value("${redis.port}")
//  private int redisPort;
//
//  @Bean
//  public LettuceConnectionFactory redisConnectionFactory() {
//    var redisStandalone = new RedisStandaloneConfiguration(redisHost, redisPort);
//    return new LettuceConnectionFactory(redisStandalone);
//  }
//
//  @Bean
//  public RedisTemplate<String, Object> redisTemplate(
//      RedisConnectionFactory redisConnectionFactory) {
//    RedisTemplate<String, Object> template = new RedisTemplate<>();
//    template.setConnectionFactory(redisConnectionFactory);
//    template.setKeySerializer(new StringRedisSerializer());
//    template.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
//    template.setHashKeySerializer(new StringRedisSerializer());
//    template.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
//    template.afterPropertiesSet();
//    return template;
//  }
//}
