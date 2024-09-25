package com.cache.booksystem;


import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Map;

public class DocumentCache {
    private final ConcurrentHashMap<String, Document> cache;
    private final PriorityQueue<Document> frequencyQueue;
    private final int maxCacheSize;

    public DocumentCache(int maxCacheSize) {
        this.cache = new ConcurrentHashMap<>();
        this.frequencyQueue = new PriorityQueue<>(Comparator.comparingInt(Document::getFrequency).thenComparing(Document::getLastAccessTime));
        this.maxCacheSize = maxCacheSize;
    }

    public static class Document implements Serializable {
        private final String documentId;
        private final String content;
        private final long ttl; // Time-To-Live
        private long lastAccessTime;
        private int frequency; // Access frequency

        public Document(String documentId, String content, long ttl) {
            this.documentId = documentId;
            this.content = content;
            this.ttl = ttl;
            this.lastAccessTime = System.currentTimeMillis();
            this.frequency = 0; // Initial frequency
        }

        public long getTTL() { return ttl; }
        public long getLastAccessTime() { return lastAccessTime; }
        public int getFrequency() { return frequency; }
        
        public void updateAccessTime() {
            this.lastAccessTime = System.currentTimeMillis();
        }
        
        public void incrementFrequency() {
            this.frequency++;
        }
    }

    public void addDocument(Document document) {
        cache.put(document.documentId, document);
        frequencyQueue.offer(document); // Add to the priority queue
        evictCacheIfNecessary();
    }

    public Document getDocument(String documentId) {
        Document document = cache.get(documentId);
        if (document != null && !isExpired(document)) {
            document.incrementFrequency();
            document.updateAccessTime();
            frequencyQueue.remove(document); // Remove to update position in queue
            frequencyQueue.offer(document); // Reinsert to maintain order
            return document;
        } else {
            cache.remove(documentId);
            frequencyQueue.remove(document); // Remove from queue if expired
            return null;
        }
    }

    private boolean isExpired(Document document) {
        return (System.currentTimeMillis() - document.getLastAccessTime() > document.getTTL());
    }

    private void evictCacheIfNecessary() {
        // Remove expired items
        cache.entrySet().removeIf(entry -> isExpired(entry.getValue()));

        // Evict least frequently accessed items if cache exceeds max size
        while (cache.size() > maxCacheSize) {
            Document leastAccessedDoc = frequencyQueue.poll(); // Get the least frequently accessed
            if (leastAccessedDoc != null) {
                cache.remove(leastAccessedDoc.documentId); // Remove from cache
            }
        }
    }
}
