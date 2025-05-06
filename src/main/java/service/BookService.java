package service;


import entities.Book;
import repository.BookRepository;

import java.sql.SQLException;
import java.util.List;

public class BookService {
    private final BookRepository repo = new BookRepository();


    public Book addBook(String title, String author, String isbn) {
        try {
            return repo.save(new Book(title, author, isbn));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Book> listBooks() {
        try {
            return repo.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
