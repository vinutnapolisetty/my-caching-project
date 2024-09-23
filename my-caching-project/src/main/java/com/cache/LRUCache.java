package com.cache;

import java.util.*;
public class LRUCache<K,V> extends LinkedHashMap<K,V> {
    private final int capacity;

    public LRUCache(int capacity)
    {
        super(capacity,0.75f,true);
        this.capacity=capacity;
    }
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest)
    {
        return size()>capacity;
    }

    public static void main(String[] args) {
        LRUCache<String,String> cache=new LRUCache<>(3);
        cache.put("1","One");
        cache.put("2","Two");
        cache.put("3","Three");
        System.out.println(cache);
        System.out.println(cache.get("1"));
        cache.put("4","Four");
        System.out.println(cache);
        
    }
    
}
