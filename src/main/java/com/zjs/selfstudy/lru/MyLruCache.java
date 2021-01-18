package com.zjs.selfstudy.lru;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public class MyLruCache<K, V> extends LruCache<K, V> {

    private Map<K, CacheNode> cache;
    private CacheNode first;
    private CacheNode last;

    public CacheNode getFirst() {
        return first;
    }

    public void setFirst(CacheNode first) {
        this.first = first;
    }

    public CacheNode getLast() {
        return last;
    }

    public void setLast(CacheNode last) {
        this.last = last;
    }

    public Map<K, CacheNode> getCache() {
        return cache;
    }

    public void setCache(Map<K, CacheNode> cache) {
        this.cache = cache;
    }

    public MyLruCache(int capacity, Storage lowSpeedStorage) {
        super(capacity, lowSpeedStorage);
        if (capacity <= 0) {
            throw new InvalidParameterException("capacity must greater than 0");
        }
        this.cache = new HashMap<K, CacheNode>(this.capacity);
    }

    public synchronized V get(K key) {
        CacheNode cacheNode = this.cache.get(key);
        if (cacheNode != null) {
            this.moveToFirst(cacheNode);
        } else {
            V value = this.lowSpeedStorage.get(key);
            cacheNode = this.addNewToCache(key, value);
        }
        return cacheNode.value;
    }

    private CacheNode addNewToCache(K key, V value) {
        while (this.cache.size() > this.capacity - 1) {
            this.cache.remove(last.key);
            last = last.prev;
            if (last == null) {
                first = null;
            }
        }
        CacheNode newFirst = new CacheNode(key, value);
        if (first == null && last == null) {
            first = newFirst;
            last = newFirst;
        } else {
            newFirst.next = first;
            first.prev = newFirst;
            first = newFirst;
        }
        this.cache.put(key, newFirst);
        return newFirst;
    }

    private void moveToFirst(CacheNode node) {
        if (!node.equals(first)) {
            node.prev.next = node.next;
            node.next = first;
            first.prev = node;
            node.prev = null;
        }
    }

    public class CacheNode {
        K key;
        V value;
        CacheNode prev;
        CacheNode next;
        public CacheNode(K key, V value) {this.key = key; this.value = value;}

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public CacheNode getPrev() {
            return prev;
        }

        public void setPrev(CacheNode prev) {
            this.prev = prev;
        }

        public CacheNode getNext() {
            return next;
        }

        public void setNext(CacheNode next) {
            this.next = next;
        }
    }

}
