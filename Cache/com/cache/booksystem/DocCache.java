package com.cache.booksystem;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.nio.file.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 */

public class DocCache implements Serializable{
    private final Map<String,Doc> cache;
    private final int CACHE_SIZE;
    private final String diskPath;
// Atomic Operations are operations that are performed atomically, meaning they are indivisible and cannot be interrupted by other threads.

    private final AtomicInteger hit=new AtomicInteger(0);
    private final AtomicInteger miss=new AtomicInteger(0);

    public DocCache(int cacheSize,String diskPath)
    {
        this.CACHE_SIZE=cacheSize;
        this.diskPath=diskPath;
        this.cache=new ConcurrentHashMap<>(CACHE_SIZE);
        try{
            Files.createDirectories(Paths.get(diskPath));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private static class Doc implements Serializable{
        private final String docId;
        private final String content;
        private final long timestamp;
        private final long lastModified;

        Doc(String docId,String content,long timestamp,long lastModified)
        {
            this.docId=docId;
            this.content=content;
            this.timestamp=System.currentTimeMillis();
            this.lastModified=System.currentTimeMillis();
        }

        @Override
        public String toString()
        {
            return "Doc{"+"docId='"+docId+'\''+",lastModified="+lastModified+'}';
        }
    }

    public Doc getDoc(String docId) throws IOException,ClassNotFoundException
    {
        Doc doc=cache.get(docId);
        if(doc!=null)
        {
            hit.incrementAndGet();
            return doc;
        }
        else{
            miss.incrementAndGet();
            doc=loadDocFromDisk(docId);
            if(doc!=null)
            {
                addToCache(docId,doc);
            }
            return doc;
        }
    }
    private Doc loadDocFromDisk(String docId) throws IOException,ClassNotFoundException
    {
        Path filePath=Paths.get(diskPath,docId);
        if(Files.exists(filePath))
        {
            try(ObjectInputStream ois=new ObjectInputStream(Files.newInputStream(filePath)))
            {
                return (Doc)ois.readObject();
            }
        }
        return null;
    }
    private void addToCache(String docId,Doc doc)
    {
        if(cache.size()>CACHE_SIZE)
        {
            evictCacheIfNecessary();
        }
        cache.put(docId,doc);
    }
    private void evictCacheIfNecessary()
    {
        while(cache.size()>CACHE_SIZE)
        {
            String docToRemove=cache.keySet().iterator().next();
            cache.remove(docToRemove);
        }
    }

    private void saveDoc(Doc doc) throws IOException
    {
        cache.put(doc.docId,doc);
        evictCacheIfNecessary();
        Path filePath=Paths.get(diskPath,doc.docId);
        try(ObjectOutputStream oos=new ObjectOutputStream(Files.newOutputStream(filePath)))
        {
            oos.writeObject(doc);
        }
    }

    public void printStats()
    {
        System.out.println("Cache Hits:"+hit.get());
        System.out.println("Cache Misses:"+miss.get());
        System.out.println("Cache Hit Ratio:"+((double)hit.get()/(hit.get()+miss.get())));
        System.out.println("Cache Size:"+cache.size());
        System.out.println("Cache Capacity:"+CACHE_SIZE);
        System.out.println("Cache Efficiency:"+((double)hit.get()/(hit.get()+miss.get())*100)+"%");
    }
    
    public static void main(String[] args)
    {
        DocCache dc=new DocCache(100,"D:\\Codebase\\my-caching-project\\my-caching-project\\src\\main\\java\\com\\cache\\booksystem\\diskstorage");
        ExecutorService executorService=Executors.newFixedThreadPool(2);
    
        for (int i = 0; i < 100; i++) {
        final int index = i;
        executorService.submit(() -> {
            try {
                String docId = "Doc" + index;
                if (index % 10 == 0) {
                    Doc d = new Doc(docId, "Content of doc" + docId, System.currentTimeMillis(), System.currentTimeMillis());
                    dc.saveDoc(d);
                } else {
                    Doc d = dc.getDoc(docId);
                    if (d != null) {
                        System.out.println("Doc retrieved from cache: " + d);
                    } else {
                        System.out.println("Doc not found: " + docId);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Thread " + index + " completed");
            }
        });
    }

    // Shutdown the executor and wait for termination
    executorService.shutdown();
    try {
        if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
            executorService.shutdownNow(); // Force shutdown if tasks take too long
        }
    } catch (InterruptedException e) {
        executorService.shutdownNow();
    }

    // Print statistics after all tasks are completed
    dc.printStats();
    }
    
}
