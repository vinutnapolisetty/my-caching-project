package com.cache.booksystem;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
//Write comment for each line of code auto generated why its is required
public class DetailDocumentCache {
    //ConcurenthashMap is used instead of hashmap because it is thread safe and it is used in concurrent environment
    //It allows multiple threads to access and modify the cache concurrently without causing any issues
    //Unlike Collections.synchronizedMap, it does not lock the entire map during write operations,
    //better performance and scalability in multi-threaded environments
    private final ConcurrentHashMap<String,Document> cache;

    private final String diskStoragePath;
    private final int maxCacheSize;

    //AutomicInteger is used instead of integer because it provides atomic operations on the integer value
    //AtomicInteger ensures that the integer value is updated atomically, which means that the value is updated in a single step without any interruptions
    //This is useful in concurrent environments where multiple threads may be accessing and updating the integer value simultaneously
    //AtomicInteger provides methods like getAndIncrement, getAndDecrement, compareAndSet, etc., which are thread-safe and do not suffer from the issues of race conditions and inconsistent reads
    private final AtomicInteger cacheHits=new AtomicInteger(0);
    //Why not volatile instead of AtomicInteger?
    //Volatile keyword is used to ensure that the value of the variable is always read from the main memory and not from the cache
    //It ensures that the variable is always up to date and visible to all threads
    //However, it does not provide atomic operations on the variable
    //Volatile keyword is useful when you want to ensure that the variable is always read from the main memory and not from the cache
    //However, it does not provide atomic operations on the variable

    //What is atomic can you give an example?
    //Atomic operations are operations that are performed in a single step and cannot be interrupted by other threads
    //AtomicInteger provides methods like getAndIncrement, getAndDecrement, compareAndSet, etc., which are thread-safe and do not suffer from the issues of race conditions and inconsistent reads  
    private final AtomicInteger cacheMisses=new AtomicInteger(0);

    public DetailDocumentCache(int maxCacheSize,String diskStoragePath){
        this.maxCacheSize=maxCacheSize;
        this.diskStoragePath=diskStoragePath;
        this.cache=new ConcurrentHashMap<>(maxCacheSize);

        try{
            Files.createDirectories(Paths.get(diskStoragePath));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public Document getDocumentIfExistElseSave(String documentId,DetailDocumentCache cache1) throws IOException,ClassNotFoundException{
        Document cachedDocument=cache.get(documentId);
        Document newDocument=null;
        if(cachedDocument!=null){
            cacheHits.incrementAndGet();
            return cachedDocument;
        }
        else{
            cacheMisses.incrementAndGet();
            
            Document document=loadDocumentFromDisk(documentId);
            if(document== null){
                newDocument=new Document(documentId,"Content for "+documentId,System.currentTimeMillis(),System.currentTimeMillis());
                cache1.saveDocument(newDocument);
                System.out.println("Saved document: "+documentId);
                return newDocument;
            }else{
                return document;
            }
            //System.out.println("getting from disk , id:" + documentId);
        }
        
    }
    public void saveDocument(Document document) throws IOException{
        
        evictCacheIfNecessary();
        saveToDisk(document);
        addToCache(document.getDocumentId(),document);
    }
    private void saveToDisk(Document document) throws IOException{
        Path filePath=Paths.get(diskStoragePath,document.getDocumentId());
        try(ObjectOutputStream out=new ObjectOutputStream(Files.newOutputStream(filePath))){
            out.writeObject(document);
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
    private void addToCache(String documentId,Document document){
        if(cache.size()>maxCacheSize){
            evictCacheIfNecessary();
        }   
        cache.put(documentId,document);
    }
    private Document loadDocumentFromDisk(String documentId) throws IOException,ClassNotFoundException{
        Path filePath=Paths.get(diskStoragePath,documentId);
        if(Files.exists(filePath)){
            try(ObjectInputStream in=new ObjectInputStream(Files.newInputStream(filePath))){
                return (Document)in.readObject();
            }   
        }
        return null;
    }
    private void evictCacheIfNecessary(){
        while(cache.size()>maxCacheSize){
            String oldestDocument =cache.keySet().iterator().next();
            cache.remove(oldestDocument);
        }
    }
    //Why static class instead of inner class?
    //Static class can be instantiated without having to instantiate the outer class
    //Static class can be used in the outer class without having to instantiate the outer class
    //Why to implement serializable interface?
    //Serializable interface is implemented to allow the Document object to be converted to a byte stream and stored in the file system
    //This allows the Document object to be saved to the file system and loaded back into the program
    public static class Document implements Serializable{
        private final String documentId;
        private final String content;
        private final long timestamp;
        private final long lastModifiedTime;

        public Document(String documentId,String content,long timestamp,long lastModifiedTime){
            this.documentId=documentId;
            this.content=content;
            this.timestamp=timestamp;
            this.lastModifiedTime=lastModifiedTime;
        }
        public String getDocumentId(){
            return documentId;
        }
        public String getContent(){
            return content;
        }
        public long getTimestamp(){
            return timestamp;
        }
        public long getLastModifiedTime(){
            return lastModifiedTime;
        }
        @Override
        public String toString(){
            return "Document{"+"documentId='"+documentId+'\''+", content='"+content+'\''+", timestamp="+timestamp+", lastModifiedTime="+lastModifiedTime+'}';
        }

    }
    public static void main(String[] args){
        DetailDocumentCache cache=new DetailDocumentCache(200,"C:\\Codebase\\my-caching-project\\my-caching-project\\src\\main\\java\\com\\cache\\boooksystem\\diskStorage");
        //How ExecutorService is better than Thread creating by extrending thread class?
        //ExecutorService is better than Thread creating by extrending thread class because it provides a more efficient and flexible way to manage threads
        //ExecutorService provides a pool of threads that can be reused to execute multiple tasks
        //It allows tasks to be executed concurrently and provides a way to control the number of threads in the pool
        //It provides a way to schedule tasks to be executed after a delay or at regular intervals
        ExecutorService executor=Executors.newFixedThreadPool(2);
        //Simulate multiple threads acessing and modifying the document
System.err.println("Starting the simulation");

        //Explain these code in detail
        //Simulate multiple threads acessing and modifying the document
        for(int i=0;i<20;i++){
        final int index=(i%10); //final created to give in lambda function in executor.
        executor.execute(()->{
            try{
                //Explain these code in detail
                //Simulate a document with a unique ID based on the index
                //Explain these code in detail
                //Simulate a document with a unique ID based on the index
                // Explain if and else condition in detail
                //If the index is divisible by 10, a new document is created and saved to the cache
                //If the index is not divisible by 10, the document is retrieved from the cache
                //Explain the purpose of the try and catch block
                //The try and catch block is used to catch any exceptions that may be thrown by the code
                //The try block is used to execute the code that may throw an exception
                //The catch block is used to catch any exceptions that may be thrown by the code
                //Explain the purpose of the if and else condition
                //The if and else condition is used to check if the index is divisible by 10
                //If the index is divisible by 10, a new document is created and saved to the cache
                //If the index is not divisible by 10, the document is retrieved from the cache
                String id = "Document"+ (index);
                if(index%4==0){
                    if(cache.loadDocumentFromDisk(id)!=null){
                        System.out.println("Document already exists: "+id);
                        System.out.println("Document: "+cache.loadDocumentFromDisk(id));
                    }
                    else{
                    Document newDocument=new Document(id,"Content for "+id,System.currentTimeMillis(),System.currentTimeMillis());
                    cache.saveDocument(newDocument);
                    System.out.println("Saved document: "+id);
                    }
                }else{
                    Document document=cache.getDocumentIfExistElseSave(id,cache);
                    if(document!=null){
                        System.out.println("Retrieved document: "+id);
                    }else{
                        System.out.println("Document not found: "+id);
                    }
                }
            }catch(IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
            finally{
                System.out.println("Thread "+index+" completed");
               // executor.shutdown();
                try{
                    executor.awaitTermination(1,TimeUnit.MINUTES);
                    executor.shutdown();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
          
            }
        });
        cache.printCacheStatistics();
    }
}
}