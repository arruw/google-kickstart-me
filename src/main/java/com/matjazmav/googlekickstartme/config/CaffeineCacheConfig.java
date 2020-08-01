package com.matjazmav.googlekickstartme.config;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.matjazmav.googlekickstartme.service.GoogleKickstartService;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
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
