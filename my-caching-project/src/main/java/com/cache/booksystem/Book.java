
package com.cache.booksystem;

import java.util.*;
class BookLib
{
    private final Map<String,Book> bookdb=new HashMap<>();
    private final Map<String,Book> cache=new HashMap<>();
    private int CACHE_SIZE=3;
    private int hit=0;
    private int miss=0;
    public BookLib()
    {
    bookdb.put("1", new Book("1","Book1","Author1"));
    bookdb.put("2", new Book("2","Book2","Author2"));
    bookdb.put("3", new Book("3","Book3","Author3"));
    bookdb.put("4", new Book("4","Book4","Author4"));
    bookdb.put("5", new Book("5","Book5","Author5"));
    }
    private static class Book{
        private String bookId;
        private String title;
        private String author;
    
        public Book(String bookId,String title,String author)
        {
            this.bookId=bookId;
            this.title=title;
            this.author=author;
        }
        @Override
        public String toString()
        {
            return "Book{bookId='"+bookId+"', title='"+title+"', author='"+author+"'}";
        }
    }
    private Book getBook(String bookId)
    {
        Book book=cache.get(bookId);
        if(book!=null)
        {
            hit++;
            System.out.println("Cache hit for bookId: "+bookId);
            return book;
        }
        else{
            miss++;
            System.out.println("Cache miss for bookId: "+bookId);
            book=bookdb.get(bookId);
            if(book!=null)
            {
                addToCache(bookId,book);
            }
            return book;
        }
    }

    private void addToCache(String bookId,Book book)
    {
        if(cache.size()>CACHE_SIZE)
        {
            String old=cache.keySet().iterator().next();
            cache.remove(old);
            System.out.println("Cache is full. Removing oldest bookId: "+old);
        }
        cache.put(bookId,book);
        System.out.println("Added to cache bookId: "+bookId);
    }

    public void printStats()
    {
        System.out.println("Cache hits: "+hit);
        System.out.println("Cache misses: "+miss);
        System.out.println("Cache hit ratio: "+(double)hit/(hit+miss));
        System.out.println("Current cache size: "+cache.size());
        System.out.println("Cache contents: "+cache.keySet());
    }
    public static void main(String[] args)
    {
        BookLib bookLib=new BookLib();
        String req[]={"1","2","3","4","5","1","2","6","7","8","9","100"};
        for(String bookId:req)
        {
            Book book=bookLib.getBook(bookId);
            if(book!=null)
            {
                System.out.println("Found: "+book);
            }
            else{
                System.out.println("Not found: "+bookId);
            }
        }
        System.out.println("---Cache Stats---");
        bookLib.printStats();
    }
}

