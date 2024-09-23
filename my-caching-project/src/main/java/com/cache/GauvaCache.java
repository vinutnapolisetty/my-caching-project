package com.cache;
import com.google.common.cache.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GauvaCache {
    public static void main(String[] args) {
        LoadingCache<String,String> cache=CacheBuilder.newBuilder().
        maximumSize(200000).
        expireAfterAccess(10,TimeUnit.MINUTES).
        build(new CacheLoader<String,String>(){
            @Override
            public String load(String key) throws Exception
            {
                return "Value of" + key;
            }
    });
        try{
            String[] type={"melody","pop","rock","jazz","soft","classical"};
            //to generate 1000 songs
            for(int i=0;i<1000;i++)
            {
                String song="Song"+i;
                String t=type[new Random().nextInt(type.length)];
                cache.put(song,t);
            }
            for(int i=0;i<10;i++)
            {
                long start=System.nanoTime();
                String song=cache.get("Song20");
                long end=System.nanoTime();
                System.out.println("Time taken to get the song is "+(end-start)+" nanoseconds");
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
    }

    
}
