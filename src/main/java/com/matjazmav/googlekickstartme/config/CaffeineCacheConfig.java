package com.matjazmav.googlekickstartme.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineCacheConfig {

    @Bean
    @Primary
    public CacheManager cacheManager() {
        return new CaffeineCacheManager(){{
            setCaffeine(Caffeine.newBuilder()
                    .expireAfterWrite(1, TimeUnit.HOURS)
                    .recordStats());
        }};
    }
}
