package com.asim.business.infrastructure.config;

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
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class CacheConfigs {

    public static final String RUNTIME_MEMORY_CACHE = "runtime";
    // Redis cache names
    public static final String INVALID_SESSION = "invalidSession";
    public static final String USERS = "user";
    public static final String AUTHORS = "author";
    public static final String BOOKS = "book";
    private static final String CACHE_NAME_PREFIX = "business:";
    private static final Long DEFAULT_EXPIRATION = 7L;// in days
    private final ObjectMapper objectMapper;
    @Value("${jwt.access.expiry}")
    private long accessJwtExpiration;

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheManager redisCacheManager = redisCacheManager(connectionFactory);
        ConcurrentMapCacheManager runtimeCacheManager = runtimeMemoryCacheManager();

        CompositeCacheManager compositeCacheManager = new CompositeCacheManager();
        compositeCacheManager.setCacheManagers(List.of(runtimeCacheManager, redisCacheManager));
        compositeCacheManager.setFallbackToNoOpCache(false);

        return compositeCacheManager;
    }


    private RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        // Configure ObjectMapper to include type information
        ObjectMapper cacheObjectMapper = objectMapper.copy();
        cacheObjectMapper.activateDefaultTyping(
                cacheObjectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL
        );

        // serializers for Redis
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(cacheObjectMapper);
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
                .initialCacheNames(Set.of(INVALID_SESSION, USERS, AUTHORS, BOOKS))
                .build();
    }

    private Map<String, RedisCacheConfiguration> customRedisCacheConfigurations(RedisCacheConfiguration defaultConfig) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        cacheConfigurations.put(INVALID_SESSION, defaultConfig.entryTtl(Duration.ofMillis(accessJwtExpiration)));
        cacheConfigurations.put(USERS, defaultConfig);
        cacheConfigurations.put(AUTHORS, defaultConfig);
        cacheConfigurations.put(BOOKS, defaultConfig);

        return cacheConfigurations;
    }

    private ConcurrentMapCacheManager runtimeMemoryCacheManager() {
        return new ConcurrentMapCacheManager(RUNTIME_MEMORY_CACHE);
    }
}