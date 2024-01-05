package by.clevertec.cach.factory;

import by.clevertec.cach.Cache;
import by.clevertec.cach.cacheImpl.LFUCacheImpl;
import by.clevertec.cach.cacheImpl.LRUCacheImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CacheFactoryImpl<K, V> implements CacheFactory<K, V> {

    @Value("#{T(java.lang.Integer).parseInt('${cache.capacity}')}")
    private Integer capacity;

    @Value("${cache.algorithm}")
    private String algorithm;

    @Override
    public Cache<K, V> createCacheType() {

         if (algorithm.equals("LRUCache")) {
            return new LRUCacheImpl<>(this.capacity);
        } else {
            return new LFUCacheImpl<>(this.capacity);
        }
    }
}