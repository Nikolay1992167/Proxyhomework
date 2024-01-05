package by.clevertec.cach.factory;

import by.clevertec.cach.Cache;

public interface CacheFactory <K,V> {

    Cache <K, V> createCacheType();
}
