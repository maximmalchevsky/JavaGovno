package service;

import entities.Book;
import repository.Repository;

import java.util.List;

public class Service {
    private final Repository repository;

    public Service(Repository repository) {
        this.repository = repository;
    }

    public Book addBook(String title, String author, String isbn, boolean is_read) {
        return repository.save(new Book(title, author, isbn, is_read));
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

    public void setRead(int id, boolean isRead) {
        System.out.println("Service.setRead: id=" + id + ", isRead=" + isRead);
        repository.setRead(id, isRead);
    }

}

