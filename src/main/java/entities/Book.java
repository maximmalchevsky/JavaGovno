package entities;

public class Book {
    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private Boolean is_read;

    // Конструкторы
    public Book(String title, String author, String isbn, Boolean is_read) {
        this(null, title, author, isbn, is_read);
    }

    public Book(Integer id, String title, String author, String isbn, Boolean is_read) {
        this.id     = id;
        this.title  = title;
        this.author = author;
        this.isbn   = isbn;
        this.is_read = is_read;
    }

    public Integer getId()    { return id; }
    public String  getTitle() { return title; }
    public String  getAuthor(){ return author; }
    public String  getIsbn()  { return isbn; }
    public Boolean  getIs_read()  { return is_read; }

    public void setId(Integer id)       { this.id = id; }
    public void setTitle(String title)  { this.title = title; }
    public void setAuthor(String author){ this.author = author; }
    public void setIsbn(String isbn)    { this.isbn = isbn; }
    public void setIs_read(Boolean is_read) { this.is_read = is_read; }

    @Override
    public String toString() {
        return title + " by " + author;
    }
}
