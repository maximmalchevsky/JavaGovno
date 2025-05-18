package service;

import entities.Book;
import repository.Repository;

import java.sql.SQLException;
import java.util.List;

public class Service {
    private final Repository repository;

    public Service(Repository repository) {
        this.repository = repository;
    }

    public Book addBook(String title, String author, String isbn) {
        return repository.save(new Book(title, author, isbn));
    }


    public List<Book> listBooks() {
        return repository.findAll();
    }

    public void deleteBook(int id) {
        repository.delete(id);
    }

    public Book updateBook(Book book) {
        return repository.update(book);
    }

    public List<Book> findBooksByAuthor(String author) {
        return repository.findByAuthor(author);
    }

}
