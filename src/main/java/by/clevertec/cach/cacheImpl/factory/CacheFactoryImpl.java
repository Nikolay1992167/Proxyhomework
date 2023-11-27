package by.clevertec.cach.cacheImpl.factory;

import by.clevertec.cach.Cache;
import by.clevertec.cach.cacheImpl.LFUCacheImpl;
import by.clevertec.cach.cacheImpl.LRUCacheImpl;

import static by.clevertec.config.LoadProperties.CACHE_ALGORITHM;
import static by.clevertec.config.LoadProperties.CACHE_CAPACITY;

public class CacheFactoryImpl<K, V> implements CacheFactory<K, V> {

    @Override
    public Cache<K, V> createCacheType() {
        return "LRUCache".equals(CACHE_ALGORITHM)
                ? new LRUCacheImpl<>(CACHE_CAPACITY)
                : new LFUCacheImpl<>(CACHE_CAPACITY);

    }
}
