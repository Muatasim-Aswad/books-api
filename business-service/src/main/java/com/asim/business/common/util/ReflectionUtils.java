package com.asim.business.common.util;

import com.asim.business.infrastructure.config.CacheConfigs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * Actual logic is in {@link ReflectionHelper}
 * This class is a wrapper to provide caching
 */
@Slf4j
@Component
@CacheConfig(cacheNames = CacheConfigs.RUNTIME_MEMORY_CACHE)
@RequiredArgsConstructor
public class ReflectionUtils {
    private final ReflectionHelper reflectionHelper;

    /**
     * Determines if a class is a custom class from the application
     * rather than a Java standard library, primitive, array, or enum.
     *
     * @param clazz The class to check
     * @return true if the class is a custom application class, false otherwise
     */
    @Cacheable(key = "'customClass-' + #clazz.name")
    public boolean isCustomClass(Class<?> clazz) {
        return reflectionHelper.isCustomClass(clazz);
    }


    /**
     * Returns an array of fields from the given class
     * Result is cached using Spring's caching mechanism
     *
     * @param clazz Class to extract fields from
     * @return Array containing the fields
     */
    @Cacheable(key = "'declaredFields-' + #clazz.name")
    public Field[] getDeclaredFields(Class<?> clazz) {
        return reflectionHelper.getDeclaredFields(clazz);
    }

    /**
     * Returns a set of field names from the given class
     * Result is cached using Spring's caching mechanism
     * Supports nested fields with dot notation
     *
     * @param clazz Class to extract field names from
     * @return Set containing the field names
     */
    @Cacheable(key = "'fieldNames-' + #clazz.name")
    public Set<String> getFieldNames(Class<?> clazz) {
        return reflectionHelper.getFieldNames(clazz, this);
    }
}