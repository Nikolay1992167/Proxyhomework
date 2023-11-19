package by.clevertec.cach.cacheImpl;

import by.clevertec.cach.Cache;
import by.clevertec.config.LoadProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;


@Data
@AllArgsConstructor
public class LRUCacheImpl<K, V> implements Cache<K, V> {

    private int capacity;

    private HashMap<K, V> data = new HashMap<>();
    LinkedList<K> order = new LinkedList<>();

    public LRUCacheImpl() {
        this.capacity = new LoadProperties().getCACHE_CAPACITY();
    }

    /**
     * Ищет значение по ключу
     *
     * @param key
     * @return найденное значение
     */
    @Override
    public V get(K key) {
        V res = data.get(key);
        if (res != null) {
            order.remove(key);
            order.addFirst(key);
        } else {
            res = null;
        }
        return res;
    }

    /**
     * Создаёт или обновляет значение по ключу
     *
     * @param key
     * @param value
     */
    @Override
    public void put(K key, V value) {
        if (order.size() >= capacity) {
            K keyRemoved = order.removeLast();
            data.remove(keyRemoved);
        }
        order.addFirst(key);
        data.put(key, value);
    }

    /**
     * Удаляет значение по ключу
     *
     * @param key
     */
    @Override
    public void delete(K key) {
        if (data.containsKey(key)) {
            data.remove(key);
            order.remove(key);
        }
    }
}
