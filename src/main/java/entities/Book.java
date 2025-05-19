package entities;

public class Book {
    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private Boolean isRead;

    //не забыть:
    // sql -> is_read
    //java -> isRead

    public Book(String title, String author, String isbn, Boolean isRead) {
        this(null, title, author, isbn, isRead);
    }

    public Book(Integer id, String title, String author, String isbn, Boolean isRead) {
        this.id     = id;
        this.title  = title;
        this.author = author;
        this.isbn   = isbn;
        this.isRead = isRead;
    }

    public Integer getId()    { return id; }
    public String  getTitle() { return title; }
    public String  getAuthor(){ return author; }
    public String  getIsbn()  { return isbn; }
    public Boolean  getIsRead()  { return isRead; }

    public void setId(Integer id)       { this.id = id; }
    public void setTitle(String title)  { this.title = title; }
    public void setAuthor(String author){ this.author = author; }
    public void setIsbn(String isbn)    { this.isbn = isbn; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    @Override
    public String toString() {
        return title + " by " + author;
    }
}
