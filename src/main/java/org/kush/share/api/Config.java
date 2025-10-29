package org.kush.share.api;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class Config {

    @Bean
    CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("public-key");
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(Duration.ofDays(1)));
        return caffeineCacheManager;
    }
}
