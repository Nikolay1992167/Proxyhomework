package by.clevertec.cach.cacheImpl;

import by.clevertec.cach.Cache;
import by.clevertec.config.LoadProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.PriorityQueue;

@Data
@AllArgsConstructor
public class LFUCacheImpl<K, V> implements Cache<K, V> {

    private int capacity;
    private HashMap<K, V> valueMap;
    private HashMap<K, Integer> frequencyMap;
    private PriorityQueue<K> queue;
    private int minFrequency;
    private HashMap<K, Long> timeMap;

    public LFUCacheImpl() {
        this.capacity = new LoadProperties().getCACHE_CAPACITY();
        this.valueMap = new HashMap<>();
        this.frequencyMap = new HashMap<>();
        this.queue = new PriorityQueue<>((k1, k2) -> {
            int frequencyCompare = frequencyMap.get(k1) - frequencyMap.get(k2);
            if (frequencyCompare == 0) {
                return timeMap.get(k1).compareTo(timeMap.get(k2));
            }
            return frequencyCompare;
        });
        this.minFrequency = 0;
        this.timeMap = new HashMap<>();
    }

    /**
     * Ищет значение по ключу
     *
     * @param key
     * @return найденное значение
     */
    @Override
    public V get(K key) {
        if (valueMap.containsKey(key)) {
            updateFrequencyAndQueue(key);
            return valueMap.get(key);
        }
        return null;
    }

    /**
     * Создаёт или обновляет значение по ключу
     *
     * @param key
     * @param value
     */
    @Override
    public void put(K key, V value) {
        if (valueMap.containsKey(key)) {
            valueMap.put(key, value);
            updateFrequencyAndQueue(key);
        } else {
            if (valueMap.size() == capacity) {
                removeLeastFrequentlyUsed();
            }
            valueMap.put(key, value);
            frequencyMap.put(key, 1);
            queue.offer(key);
            minFrequency = 1;
            timeMap.put(key, System.currentTimeMillis());
        }
    }

    /**
     * Удаляет значение по ключу
     *
     * @param key
     */
    @Override
    public void delete(K key) {
        if (valueMap.containsKey(key)) {
            valueMap.remove(key);
            frequencyMap.remove(key);
            queue.remove(key);
            timeMap.remove(key);
        }
    }

    /**
     * Обновляет частоту и очередь положения объекта в cache
     *
     * @param key
     */
    private void updateFrequencyAndQueue(K key) {
        int frequency = frequencyMap.get(key) + 1;
        frequencyMap.put(key, frequency);
        queue.remove(key);
        queue.offer(key);
        if (frequency - 1 == minFrequency && !frequencyMap.containsValue(minFrequency)) {
            minFrequency = frequency;
        }
        timeMap.put(key, System.currentTimeMillis());
    }

    /**
     * Удаляет наименее часто используемый объект
     */
    private void removeLeastFrequentlyUsed() {
        K key = queue.poll();
        valueMap.remove(key);
        frequencyMap.remove(key);
        timeMap.remove(key);
    }
}