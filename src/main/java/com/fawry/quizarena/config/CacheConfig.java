package com.fawry.quizarena.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    // Redis connection + RedisCacheManager come from
    // spring-boot-starter-data-redis auto-configuration,
    // driven by application.yml (spring.cache.type=redis, spring.data.redis.*)
}