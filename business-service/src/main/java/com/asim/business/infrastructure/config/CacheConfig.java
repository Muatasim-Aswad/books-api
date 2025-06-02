package com.asim.business.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor  // Add this to inject the ObjectMapper
public class CacheConfig {

    private final ObjectMapper objectMapper;  // Inject your configured ObjectMapper

    // Cache names constants
    public static final String RUNTIME_MEMORY_CACHE = "runtimeMemoryCache";
    public static final String REDIS_ONE_HOUR_CACHE = "redisOneHourCache";
    public static final String REDIS_ONE_DAY_CACHE = "redisOneDayCache";

    /**
     * Runtime memory cache manager using ConcurrentHashMap
     * Fast access but limited to single JVM instance
     */
    @Bean
    @ConditionalOnProperty(name = "cache.type", havingValue = "memory", matchIfMissing = false)
    public CacheManager memoryCacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setCacheNames(java.util.List.of(RUNTIME_MEMORY_CACHE));
        cacheManager.setAllowNullValues(false);
        return cacheManager;
    }

    /**
     * Redis cache manager with multiple cache configurations
     * Supports distributed caching with different TTL settings
     */
    @Bean
    @Primary
    @ConditionalOnProperty(name = "cache.type", havingValue = "redis", matchIfMissing = true)
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        // Use your configured ObjectMapper for Redis serialization
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // Default configuration
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(jsonSerializer))
                .disableCachingNullValues();

        // One hour TTL configuration
        RedisCacheConfiguration oneHourConfig = defaultConfig
                .entryTtl(Duration.ofHours(1));

        // One day TTL configuration
        RedisCacheConfiguration oneDayConfig = defaultConfig
                .entryTtl(Duration.ofDays(1));

        // Cache-specific configurations
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put(REDIS_ONE_HOUR_CACHE, oneHourConfig);
        cacheConfigurations.put(REDIS_ONE_DAY_CACHE, oneDayConfig);

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware()
                .build();
    }

    /**
     * Composite cache manager that can handle both memory and Redis caches
     * Useful for hybrid caching strategies
     */
    @Bean
    @ConditionalOnProperty(name = "cache.type", havingValue = "hybrid")
    public CacheManager hybridCacheManager(RedisConnectionFactory connectionFactory) {
        // Memory cache for frequently accessed small data
        ConcurrentMapCacheManager memoryCacheManager = new ConcurrentMapCacheManager();
        memoryCacheManager.setCacheNames(java.util.List.of(RUNTIME_MEMORY_CACHE));
        memoryCacheManager.setAllowNullValues(false);

        // Use your configured ObjectMapper for Redis serialization
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // Redis cache configurations
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(jsonSerializer))
                .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put(REDIS_ONE_HOUR_CACHE, defaultConfig.entryTtl(Duration.ofHours(1)));
        cacheConfigurations.put(REDIS_ONE_DAY_CACHE, defaultConfig.entryTtl(Duration.ofDays(1)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware()
                .build();
    }
}