package ru.otus.cachehw;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private final Map<K, V> data = new WeakHashMap<>();
    private final List<HwListener<K,V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        data.put(key, value);
        notifyListeners(key, value, "Add to cache");
    }

    @Override
    public void remove(K key) {
        final V value = data.remove(key);
        notifyListeners(key, value, "Remove from cache");
    }

    @Override
    public V get(K key) {
        var value = data.get(key);
        if (value != null) {
            notifyListeners(key, value, "READ from cache");
        }
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String action) {
        listeners.forEach(listener -> {
            try {
                listener.notify(key, value, action);
            } catch (Exception e) {
                throw new MyCacheException(e);
            }
        });
    }
}
