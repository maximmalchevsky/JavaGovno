package repository;

import entities.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repository {
    private final Database db;


    public Repository(Database db) {
        this.db = db;
    }

    public Database getDb() {
        return db;
    }


    public Book save(Book book) {
        String sql = "INSERT INTO books(title, author, isbn, is_read) VALUES(?,?,?,?) RETURNING id";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setBoolean(4, book.getIs_read());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) book.setId(rs.getInt(1));
            }
            return book;

        } catch (SQLException e) {
            throw new RuntimeException("Cannot save book", e);
        }
    }


    public List<Book> findAll() {
        String sql = "SELECT id, title, author, isbn, is_read FROM books ORDER BY id";
        try (Connection c = db.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            List<Book> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getBoolean("is_read")
                ));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Cannot load books", e);
        }
    }


    public void delete(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Cannot delete book " + id, e);
        }
    }

    // Обновить данные книги
    public Book update(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, is_read = ? WHERE id = ?\n";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setInt(4, book.getId());

            int affected = ps.executeUpdate();
            if (affected != 1)                         // на всякий случай
                throw new RuntimeException("Book id " + book.getId() + " not found");

            return book;

        } catch (SQLException e) {
            throw new RuntimeException("Cannot update book " + book.getId(), e);
        }
    }

    public List<Book> findByAuthor(String author) {
        String sql = "SELECT id, title, author, isbn FROM books WHERE author ILIKE ? ORDER BY id";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, "%" + author + "%"); // позволяет искать по части имени

            try (ResultSet rs = ps.executeQuery()) {
                List<Book> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getBoolean("is_read")

                    ));
                }
                return result;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Cannot find books by author: " + author, e);
        }
    }

}
