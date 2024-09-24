package com.cache.booksystem;

import java.util.*;
import java.io.*;
import java.util.concurrent.*;


public class BookLibSys {
    public static void main(String[] args) 
    {
        BookLib bl=new BookLib();
        bl.addBook(new Book("1","Harry Potter", "J.K. Rowling", 1997));
        bl.addBook(new Book("2","To Kill a Mockingbird", "Harper Lee", 1960));
        bl.addBook(new Book("3","1984", "George Orwell", 1949));
        bl.addBook(new Book("4","Pride and Prejudice", "Jane Austen", 1813));
        bl.addBook(new Book("5","The Great Gatsby", "F. Scott Fitzgerald", 1925));
        bl.addBook(new Book("6","Wuthering Heights", "Emily Brontë", 1847));
        bl.addBook(new Book("7","Jane Eyre", "Charlotte Brontë", 1847));
        bl.addBook(new Book("8","The Catcher in the Rye", "J.D. Salinger", 1951));
        bl.addBook(new Book("9","To the Lighthouse", "Virginia Woolf", 1927));

        System.out.println(bl.getBooks());
        System.out.println(bl.getAuthors());
        System.out.println(bl.getCountByAuthor());

        Future<Book> f=bl.getPopAsync();
        try{
            Book b=f.get();
            System.out.println("Most popular book is: "+b);
    
        }
        catch(InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }
        
    }
    private static class BookLib{
        private final List<Book> book;
        private final Set<String> author;
        private final Map<String,Integer> countByauth;
        private final ExecutorService executor;
        BookLib()
        {
            this.book=new ArrayList<>();
            this.author=new HashSet<>();
            this.countByauth=new HashMap<>();
            this.executor=Executors.newFixedThreadPool(10);
        }
        public void addBook(Book b)
        {
            book.add(b);
            author.add(b.author);
            countByauth.put(b.author,countByauth.getOrDefault(b.author,0)+1);
        }
        public List getBooks()
        {
            return new ArrayList<>(book);
        }
        public Set getAuthors()
        {
            return new HashSet<>(author);
        }
        public Map getCountByAuthor()
        {
            return new HashMap<>(countByauth);
        }   
        private Book getPopBook()
        {
            return book.get(0);
        }
        public Future<Book> getPopAsync(){
            return executor.submit(()->{
                Thread.sleep(2000);
    
                return book.isEmpty()?null:getPopBook();
            });
    }
    
}
private static class Book implements Serializable{
    private final String id;
    private final String title;
    private final String author;
    private final int year;
    Book(String id,String title,String author,int year)
    {
        this.id=id;
        this.title=title;
        this.author=author;
        this.year=year;
    }
    transient int currentPage;
    @Override
    public String toString()
    {
        return "Book{"+"id='"+id+'\''+", title='"+title+'\''+", author='"+author+'\''+", year="+year+'}';
    }
}
}
