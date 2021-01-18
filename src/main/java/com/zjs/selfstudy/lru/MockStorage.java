package com.zjs.selfstudy.lru;

import java.util.HashMap;
import java.util.Map;

public class MockStorage<K, V> implements Storage<K, V> {
    Map<K, V> storage;

    public MockStorage() {
        this.storage = new HashMap<K, V>();
    }

    public V get(K key) {
        return storage.get(key);
    }

    public void put(K key, V value) {
        storage.put(key, value);
    }
}
