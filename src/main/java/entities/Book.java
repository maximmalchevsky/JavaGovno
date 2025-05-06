package entities;

public class Book {
    private Integer id;
    private String title;
    private String author;
    private String isbn;

    // Конструкторы
    public Book(String title, String author, String isbn) {
        this(null, title, author, isbn);
    }

    public Book(Integer id, String title, String author, String isbn) {
        this.id     = id;
        this.title  = title;
        this.author = author;
        this.isbn   = isbn;
    }

    public Integer getId()    { return id; }
    public String  getTitle() { return title; }
    public String  getAuthor(){ return author; }
    public String  getIsbn()  { return isbn; }

    public void setId(Integer id)       { this.id = id; }
    public void setTitle(String title)  { this.title = title; }
    public void setAuthor(String author){ this.author = author; }
    public void setIsbn(String isbn)    { this.isbn = isbn; }

    @Override
    public String toString() {
        return title + " by " + author;
    }
}
