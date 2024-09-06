package com.library.persistence.dao;

import com.library.business.document.Book;

import java.sql.*;
import java.util.List;

public class BookDAO extends DocumentDAO<Book> {

    @Override
    public Book findById(String id) {
        List<Book> results = executeQuery("SELECT * FROM books WHERE id = ?", id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Book> findAll() {
        return executeQuery("SELECT * FROM books");
    }

    @Override
    public void save(Book book) {
        String sql = "INSERT INTO books (id, title, author, publication_date, isbn, pages) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getId());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setDate(4, Date.valueOf(book.getPublicationDate()));
            stmt.setString(5, book.getIsbn());
            stmt.setInt(6, book.getPages());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, publication_date = ?, isbn = ?, pages = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setDate(3, Date.valueOf(book.getPublicationDate()));
            stmt.setString(4, book.getIsbn());
            stmt.setInt(5, book.getPages());
            stmt.setString(6, book.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM books WHERE id = ?")) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Book createFromResultSet(ResultSet rs) throws SQLException {
        return new Book(
            rs.getString("id"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getDate("publication_date").toLocalDate(),
            rs.getString("isbn"),
            rs.getInt("pages")
        );
    }
}
