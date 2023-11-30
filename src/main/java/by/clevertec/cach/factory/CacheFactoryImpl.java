package by.clevertec.cach.factory;

import by.clevertec.cach.Cache;
import by.clevertec.cach.cacheImpl.LFUCacheImpl;
import by.clevertec.cach.cacheImpl.LRUCacheImpl;

import static by.clevertec.config.LoadProperties.CACHE_ALGORITHM;
import static by.clevertec.config.LoadProperties.CACHE_CAPACITY;

public class CacheFactoryImpl<K, V> implements CacheFactory<K, V> {

    private static final String LRU_CACHE = "LRUCache";

    @Override
    public Cache<K, V> createCacheType() {
        return LRU_CACHE.equals(CACHE_ALGORITHM)
                ? new LRUCacheImpl<>(CACHE_CAPACITY)
                : new LFUCacheImpl<>(CACHE_CAPACITY);

    }
}
