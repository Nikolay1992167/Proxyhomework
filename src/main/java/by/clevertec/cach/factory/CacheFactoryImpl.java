package by.clevertec.cach.factory;

import by.clevertec.cach.Cache;
import by.clevertec.cach.cacheImpl.LFUCacheImpl;
import by.clevertec.cach.cacheImpl.LRUCacheImpl;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import static by.clevertec.config.LoadProperties.CACHE_ALGORITHM;
import static by.clevertec.config.LoadProperties.CACHE_CAPACITY;

@Component
@PropertySource("classpath:application.properties")
public class CacheFactoryImpl<K, V> implements CacheFactory<K, V> {

    private final Environment env;
    private static final String LRU_CACHE = "LRUCache";

    public CacheFactoryImpl(Environment env) {
        this.env = env;
    }

    @Override
    public Cache<K, V> createCacheType() {

        return LRU_CACHE.equals(env.getRequiredProperty("cache.algorithm"))
                ? new LRUCacheImpl<>(Integer.parseInt(env.getRequiredProperty("cache.capacity")))
                : new LFUCacheImpl<>(Integer.parseInt(env.getRequiredProperty("cache.capacity")));
    }
}
