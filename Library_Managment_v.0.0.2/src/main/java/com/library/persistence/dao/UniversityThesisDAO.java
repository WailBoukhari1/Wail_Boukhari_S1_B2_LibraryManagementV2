package com.library.persistence.dao;

import com.library.business.document.UniversityThesis;

import java.sql.*;
import java.util.List;

public class UniversityThesisDAO extends DocumentDAO<UniversityThesis> {

    @Override
    public UniversityThesis findById(String id) {
        List<UniversityThesis> results = executeQuery("SELECT * FROM university_theses WHERE id = ?", id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<UniversityThesis> findAll() {
        return executeQuery("SELECT * FROM university_theses");
    }

    @Override
    public void save(UniversityThesis thesis) {
        String sql = "INSERT INTO university_theses (id, title, author, publication_date, university, degree) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, thesis.getId());
            stmt.setString(2, thesis.getTitle());
            stmt.setString(3, thesis.getAuthor());
            stmt.setDate(4, Date.valueOf(thesis.getPublicationDate()));
            stmt.setString(5, thesis.getUniversity());
            stmt.setString(6, thesis.getDegree());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(UniversityThesis thesis) {
        String sql = "UPDATE university_theses SET title = ?, author = ?, publication_date = ?, university = ?, degree = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, thesis.getTitle());
            stmt.setString(2, thesis.getAuthor());
            stmt.setDate(3, Date.valueOf(thesis.getPublicationDate()));
            stmt.setString(4, thesis.getUniversity());
            stmt.setString(5, thesis.getDegree());
            stmt.setString(6, thesis.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM university_theses WHERE id = ?")) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected UniversityThesis createFromResultSet(ResultSet rs) throws SQLException {
        return new UniversityThesis(
            rs.getString("id"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getDate("publication_date").toLocalDate(),
            rs.getString("university"),
            rs.getString("degree")
        );
    }
}
