package com.cache;

import java.util.HashMap;
import java.util.Map;


public class SimpleCache<K,V> {
    private final Map<K,V> cache;
    public SimpleCache()
    {
        this.cache = new HashMap<>();
    }
    public void put(K key, V value)
    {
        cache.put(key, value);
    }
    public V get(K key)
    {
        return cache.get(key);
    }
    public void clear()
    {
        cache.clear();
    }
    public boolean containsKey(K key)
    {
        return cache.containsKey(key);
    }
    public void remove(K key)
    {
        cache.remove(key);
    }
    public int size()
    {
        return cache.size();
    }
    public void printCache()
    {
        System.out.println(cache);
    }
    public static void main(String[] args) {
        SimpleCache<String,String> cache=new SimpleCache<>();
        cache.put("1","One");
        cache.put("2","Two");
        cache.put("3","Three");
        System.out.println(cache.get("1"));
        System.out.println(cache.size());
        cache.remove("1");
        cache.printCache();
        cache.clear();
    }
}
