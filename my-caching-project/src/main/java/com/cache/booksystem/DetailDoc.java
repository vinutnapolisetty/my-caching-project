package com.cache.booksystem;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.cache.booksystem.DetailDocumentCache.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
public class DetailDoc {
    private final ConcurrentHashMap<String,Doc> cache;
    private final String diskStoragePath;
    private final int maxCacheSize;
    private final AtomicInteger cacheHits=new AtomicInteger(0);
    private final AtomicInteger cacheMisses=new AtomicInteger(0);
    
    //Constructor
    public DetailDoc(int maxCacheSize,String diskStoragePath){
        this.maxCacheSize=maxCacheSize;
        this.diskStoragePath=diskStoragePath;
        this.cache=new ConcurrentHashMap<>(maxCacheSize);
        try{
            Files.createDirectories(Paths.get(diskStoragePath));
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
    }

    private void addDoc(Doc doc){
        if (doc.id == null || doc == null) {
            throw new IllegalArgumentException("id or doc cannot be null");
        }
        if(!cache.containsKey(doc.id)){
        if(cache.size()>maxCacheSize){
            evictCacheIfNecessary();
        }
        cache.put(doc.id,doc);
    }
    }
    private void evictCacheIfNecessary(){
        while(cache.size()>maxCacheSize){
            String old=cache.keySet().iterator().next();
            cache.remove(old);
        }
    }
    private void saveDoc(Doc doc) throws IOException{
        evictCacheIfNecessary();
        saveToDisk(doc);
        addDoc(doc);
    }

    private void saveToDisk(Doc doc) throws IOException{
        Path fp=Paths.get(diskStoragePath,doc.id);
        try(ObjectOutputStream out=new ObjectOutputStream(Files.newOutputStream(fp))){
            out.writeObject(doc);
        }
    }

    private Doc loadDocFromDisk(String id) throws IOException,ClassNotFoundException{
        Path fp=Paths.get(diskStoragePath,id);
        if(Files.exists(fp)){
            try(ObjectInputStream in=new ObjectInputStream(Files.newInputStream(fp))){
                return (Doc)in.readObject();
            }
        }
        return null;
    }
    private Doc getDoc(String id,DetailDocumentCache cache1) throws IOException,ClassNotFoundException{
        if(id==null){
            throw new IllegalArgumentException("id cannot be null");
        }
        Doc doc=cache.get(id);
        if(doc!=null){
            cacheHits.incrementAndGet();
            return doc;
        }
        else{
            cacheMisses.incrementAndGet();
            Doc document=loadDocFromDisk(id);
            if(document== null){
                document=new Doc(id,"Content for "+id,"Author for "+id,"Content for "+id);
                cache1.saveDoc(document);
                System.out.println("Saved document: "+id);
                return document;
            }else{
                return document;
            }
            doc=loadDocFromDisk(id);
            saveDoc(doc);
            addDoc(doc);
        }
    }

    public void printCacheStatistics(){
        System.out.println("Cache Hits: "+cacheHits.get());
        System.out.println("Cache Misses: "+cacheMisses.get());
        System.out.println("Cache Size: "+cache.size());
        System.out.println("Cache Capacity: "+maxCacheSize);
        System.out.println("Cache Efficiency: "+((double)cacheHits.get()/(cacheHits.get()+cacheMisses.get())*100)+"%");
        System.out.println("Cache Hit Ratio: "+((double)cacheHits.get()/(cacheHits.get()+cacheMisses.get())*100)+"%");
        System.out.println("Cache Miss Ratio: "+((double)cacheMisses.get()/(cacheHits.get()+cacheMisses.get())*100)+"%");
    
    }

    class Doc{
        private String title;
        private String author;
        private String content;
        private String id;
        
        public Doc(String id,String title,String author,String content){
            this.id=id;
            this.title=title;
            this.author=author;
            this.content=content;
        }
    }

}
