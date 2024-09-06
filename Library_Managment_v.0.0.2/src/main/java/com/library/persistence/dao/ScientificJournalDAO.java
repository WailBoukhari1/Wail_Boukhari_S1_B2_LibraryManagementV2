package com.library.persistence.dao;

import com.library.business.document.ScientificJournal;

import java.sql.*;
import java.util.List;

public class ScientificJournalDAO extends DocumentDAO<ScientificJournal> {

    @Override
    public ScientificJournal findById(String id) {
        List<ScientificJournal> results = executeQuery("SELECT * FROM scientific_journals WHERE id = ?", id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<ScientificJournal> findAll() {
        return executeQuery("SELECT * FROM scientific_journals");
    }

    @Override
    public void save(ScientificJournal journal) {
        String sql = "INSERT INTO scientific_journals (id, title, author, publication_date, issn, research_field) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, journal.getId());
            stmt.setString(2, journal.getTitle());
            stmt.setString(3, journal.getAuthor());
            stmt.setDate(4, Date.valueOf(journal.getPublicationDate()));
            stmt.setString(5, journal.getIssn());
            stmt.setString(6, journal.getResearchField());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(ScientificJournal journal) {
        String sql = "UPDATE scientific_journals SET title = ?, author = ?, publication_date = ?, issn = ?, research_field = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, journal.getTitle());
            stmt.setString(2, journal.getAuthor());
            stmt.setDate(3, Date.valueOf(journal.getPublicationDate()));
            stmt.setString(4, journal.getIssn());
            stmt.setString(5, journal.getResearchField());
            stmt.setString(6, journal.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM scientific_journals WHERE id = ?")) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ScientificJournal createFromResultSet(ResultSet rs) throws SQLException {
        return new ScientificJournal(
            rs.getString("id"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getDate("publication_date").toLocalDate(),
            rs.getString("issn"),
            rs.getString("research_field")
        );
    }
}
