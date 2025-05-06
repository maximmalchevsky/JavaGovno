package repository;

import entities.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class BookRepository {
    public Book save(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, isbn) VALUES (?, ?, ?) RETURNING id";
        try (Connection c = ConnectionManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                book.setId(rs.getInt("id"));
            }
            return book;
        }
    }

    public List<Book> findAll() throws SQLException {
        String sql = "SELECT id, title, author, isbn FROM books";
        try (Connection c = ConnectionManager.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            List<Book> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn")
                ));
            }
            return list;
        }
    }


    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection c = ConnectionManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Обновить данные книги
    public Book update(Book book) throws SQLException {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ? WHERE id = ?";
        try (Connection c = ConnectionManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setInt(4, book.getId());
            ps.executeUpdate();
            return book;
        }
    }

}
