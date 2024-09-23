package com.cache;
import java.io.*;
import java.util.*;

public class MovieRecommendationCache {

     //L1 Cache Implementation
     private static final int MAX_ENTRIES = 100;
     private final Map<String, String> recentCache =
         new LinkedHashMap<String, String>(100, 0.75f, true){
             protected boolean removeEldestEntry(Map.Entry<String, String> eldest){
                 return size() > MAX_ENTRIES;
             }
         };
     //L2 Cache Implementation
     private static final int MAX_ENTRIES_L2 = 1000;
     private final Map<String, String> popularCache =
         new LinkedHashMap<String, String>(MAX_ENTRIES_L2, 0.75f, true){
             protected boolean removeEldestEntry(Map.Entry<String, String> eldest){
                 return size() > MAX_ENTRIES_L2;
             }
         };

         public static void main(String[] args) {
             
            Map<String, String> recentCache =
              new LinkedHashMap<String, String>(100, 0.75f, true);
              
          //L2 Cache Implementation
           
            Map<String, String> popularCache =
              new LinkedHashMap<String, String>(1000, 0.75f, true);      
              
              String[] genres= {"Action", "Comedy", "Drama", "Horror", "Romance", "Sci-Fi", "Thriller"};
              //Write code to generate 1000 movies with random genres and push in popularMoviewCache
              for(int i=0; i<2000; i++){
                  String movie = "Movie" + i;
                  String genre = genres[new Random().nextInt(genres.length)];
                  popularCache.put(movie, genre);
              }
              //Write code to generate 100 recentlye viewed movies and push in recentMoviewCache
              for(int i=0; i<100; i++){
                  String movie = "Movie" + i;
                  recentCache.put(movie, "Recently Viewed");
              }   
              //Write code to print it with time it takes to fetch the movie form map
              long startTime = System.nanoTime();
              String movie = popularCache.get("Movie100");
              long endTime = System.nanoTime();
              System.out.println("Time taken to fetch the movie: " + (endTime - startTime) + " nanoseconds");
              }
    
}
