//package com.asim.books.test.config;
//
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Primary;
//
//@TestConfiguration
//@EnableCaching
//public class TestCacheConfig {
//
//    @Bean
//    @Primary
//    public CacheManager cacheManager() {
//        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
//        cacheManager.setCacheNames(java.util.Arrays.asList("authors", "books", "fieldNames"));
//        return cacheManager;
//    }
//}