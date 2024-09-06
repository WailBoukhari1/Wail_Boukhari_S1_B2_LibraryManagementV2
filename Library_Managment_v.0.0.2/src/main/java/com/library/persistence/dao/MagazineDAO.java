package com.library.persistence.dao;

import com.library.business.document.Magazine;

import java.sql.*;
import java.util.List;

public class MagazineDAO extends DocumentDAO<Magazine> {

    @Override
    public Magazine findById(String id) {
        List<Magazine> results = executeQuery("SELECT * FROM magazines WHERE id = ?", id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Magazine> findAll() {
        return executeQuery("SELECT * FROM magazines");
    }

    @Override
    public void save(Magazine magazine) {
        String sql = "INSERT INTO magazines (id, title, author, publication_date, issn, issue_number) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, magazine.getId());
            stmt.setString(2, magazine.getTitle());
            stmt.setString(3, magazine.getAuthor());
            stmt.setDate(4, Date.valueOf(magazine.getPublicationDate()));
            stmt.setString(5, magazine.getIssn());
            stmt.setInt(6, magazine.getIssueNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Magazine magazine) {
        String sql = "UPDATE magazines SET title = ?, author = ?, publication_date = ?, issn = ?, issue_number = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, magazine.getTitle());
            stmt.setString(2, magazine.getAuthor());
            stmt.setDate(3, Date.valueOf(magazine.getPublicationDate()));
            stmt.setString(4, magazine.getIssn());
            stmt.setInt(5, magazine.getIssueNumber());
            stmt.setString(6, magazine.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM magazines WHERE id = ?")) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Magazine createFromResultSet(ResultSet rs) throws SQLException {
        return new Magazine(
            rs.getString("id"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getDate("publication_date").toLocalDate(),
            rs.getString("issn"),
            rs.getInt("issue_number")
        );
    }
}
