package entities;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;


    public Book() { }

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public Book(int id, String title, String author, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }


    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}


