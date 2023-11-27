package by.clevertec.cach.cacheImpl;

import by.clevertec.cach.Cache;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Data
public class LFUCacheImpl<K, V> implements Cache<K, V> {

    private final int capacity;
    private final Map<K, V> valueMap;
    private final Map<K, Integer> frequencyMap;
    private final Map<Integer, Set<K>> frequencySets;
    private int minFrequency;

    /**
     * Constructs a new LFUCache with the specified capacity.
     *
     * @param capacity the maximum number of entries that this cache can hold
     */
    public LFUCacheImpl(int capacity) {
        this.capacity = capacity;
        this.valueMap = new HashMap<>();
        this.frequencyMap = new HashMap<>();
        this.frequencySets = new HashMap<>();
        this.minFrequency = 0;
    }

    /**
     * Returns the value associated with the specified key in this cache,
     * or null if the cache contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null if this cache contains no mapping for the key
     */
    @Override
    public V get(K key) {

        if (!valueMap.containsKey(key)) {
            return null;
        }
        int frequency = frequencyMap.get(key);
        frequencyMap.put(key, frequency + 1);
        frequencySets.get(frequency).remove(key);
        if (frequency == minFrequency && frequencySets.get(frequency).isEmpty()) {
            minFrequency++;
        }
        frequencySets.computeIfAbsent(frequency + 1, k -> new LinkedHashSet<>()).add(key);
        return valueMap.get(key);
    }

    /**
     * Creates the specified value with the specified key in this cache.
     * If the cache previously contained a mapping for the key, the old value is replaced.
     * If the cache is full, the least frequently used entry will be removed.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with the specified key, or null if there was no mapping for the key
     */
    @Override
    public V put(K key, V value) {

        if (capacity <= 0) {
            return null;
        }
        if (valueMap.containsKey(key)) {
            V oldValue = valueMap.get(key);
            valueMap.put(key, value);
            get(key);
            return oldValue;
        }
        if (valueMap.size() >= capacity) {
            K removingKey = frequencySets.get(minFrequency).iterator().next();
            frequencySets.get(minFrequency).remove(removingKey);
            valueMap.remove(removingKey);
            frequencyMap.remove(removingKey);
        }
        valueMap.put(key, value);
        frequencyMap.put(key, 1);
        frequencySets.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
        minFrequency = 1;
        return null;
    }

    /**
     * Removes the entry for the specified key from this cache if present.
     * Returns the value to which this cache previously associated the key,
     * or null if the cache contained no mapping for the key.
     *
     * @param key the key whose mapping is to be removed from the cache
     * @return the previous value associated with key, or null if there was no mapping for key.
     */
    @Override
    public V delete(K key) {

        if (!valueMap.containsKey(key)) {
            return null;
        }
        int frequency = frequencyMap.get(key);
        frequencyMap.remove(key);
        frequencySets.get(frequency).remove(key);
        if (frequency == minFrequency && frequencySets.get(frequency).isEmpty()) {
            minFrequency++;
        }
        return valueMap.remove(key);
    }
}