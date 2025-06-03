package com.asim.auth.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CacheConfigs {

    private final ObjectMapper objectMapper;

    @Value("${jwt.refresh.expiry}")
    private long refreshJwtExpiration;

    private static final String CACHE_NAME_PREFIX = "auth:";
    private static final Long DEFAULT_EXPIRATION = 7L;// in days

    public static final String RUNTIME_MEMORY_CACHE = "runtime";

    // Redis cache names
    public static final String INVALID_SESSION = "invalidSession";
    public static final String USERS = "user";

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheManager redisCacheManager = redisCacheManager(connectionFactory);
        ConcurrentMapCacheManager runtimeCacheManager = runtimeMemoryCacheManager();

        CompositeCacheManager compositeCacheManager = new CompositeCacheManager();
        compositeCacheManager.setCacheManagers(List.of(redisCacheManager, runtimeCacheManager));
        compositeCacheManager.setFallbackToNoOpCache(false);

        return compositeCacheManager;
    }


    private RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        // serializers for Redis
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(stringSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(jsonSerializer))
                .entryTtl(Duration.ofDays(DEFAULT_EXPIRATION))
                .prefixCacheNameWith(CACHE_NAME_PREFIX)
                .disableCachingNullValues();

        var customRedisCacheConfigurations = customRedisCacheConfigurations(defaultConfig);

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(customRedisCacheConfigurations)
                .transactionAware()
                .build();
    }

    private Map<String, RedisCacheConfiguration> customRedisCacheConfigurations(RedisCacheConfiguration defaultConfig) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        cacheConfigurations.put(INVALID_SESSION, defaultConfig.entryTtl(Duration.ofMillis(refreshJwtExpiration)));
        cacheConfigurations.put(USERS, defaultConfig);

        return cacheConfigurations;
    }

    private ConcurrentMapCacheManager runtimeMemoryCacheManager() {
        return new ConcurrentMapCacheManager(RUNTIME_MEMORY_CACHE);
    }
}