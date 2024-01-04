package by.clevertec.cach.cacheImpl;

import by.clevertec.cach.Cache;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = false)
public class LRUCacheImpl<K, V> extends LinkedHashMap<K, V> implements Cache<K, V> {

    private final int capacity;

    /**
     * Constructs a new LRUCache with the specified capacity.
     *
     * @param capacity the maximum number of entries that this cache can hold
     */
    public LRUCacheImpl(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    /**
     * Removes the eldest entry from the cache when it reaches its capacity.
     *
     * @param eldest the least recently used entry in the map
     * @return true if the eldest entry should be removed from the map, false otherwise
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > this.capacity;
    }

    /**
     * Removes the entry for the specified key from this cache if present.
     * Returns the value to which this cache previously associated the key,
     * or null if the cache contained no mapping for the key.
     *
     * @param key the key whose mapping is to be removed from the cache
     * @return the previous value associated with key, or null if there was no mapping for key.
     */
    public V delete(K key) {
        return remove(key);
    }
}
