package com.library.dao;

import com.library.model.document.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DocumentDAO {
    private static DocumentDAO instance;

    private DocumentDAO() {}

    public static DocumentDAO getInstance() {
        if (instance == null) {
            instance = new DocumentDAO();
        }
        return instance;
    }

    public void save(Document document) {
        String sql = "INSERT INTO library_documents (id, title, author, publisher, publication_year) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            UUID id = UUID.randomUUID();
            stmt.setObject(1, id);
            stmt.setString(2, document.getTitle());
            stmt.setString(3, document.getAuthor());
            stmt.setString(4, document.getPublisher());
            stmt.setInt(5, document.getPublicationYear());
            stmt.executeUpdate();

            saveSpecificDocument(id, document);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean documentExists(String title) {
        String sql = "SELECT COUNT(*) FROM library_documents WHERE title = ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private void saveSpecificDocument(UUID id, Document document) throws SQLException {
        String tableName = getTableName(document);
        String sql = String.format("INSERT INTO %s (id, title, author, publisher, publication_year, type) VALUES (?, ?, ?, ?, ?, ?)", tableName);
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            stmt.setString(2, document.getTitle());
            stmt.setString(3, document.getAuthor());
            stmt.setString(4, document.getPublisher());
            stmt.setInt(5, document.getPublicationYear());
            stmt.setString(6, document.getType());
            
            if (document instanceof ScientificJournal) {
                stmt.setString(7, ((ScientificJournal) document).getResearchField());
            } else if (document instanceof UniversityThesis) {
                stmt.setString(7, ((UniversityThesis) document).getUniversity());
                stmt.setString(8, ((UniversityThesis) document).getField());
            }
            
            stmt.executeUpdate();
        }
    }

    public void update(Document document) {
        String sql = "UPDATE library_documents SET author = ?, publisher = ?, publication_year = ? WHERE title = ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, document.getAuthor());
            stmt.setString(2, document.getPublisher());
            stmt.setInt(3, document.getPublicationYear());
            stmt.setString(4, document.getTitle());
            stmt.executeUpdate();

            updateSpecificDocument(document);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateSpecificDocument(Document document) throws SQLException {
        String tableName = getTableName(document);
        String sql = String.format("UPDATE %s SET author = ?, publisher = ?, publication_year = ? WHERE title = ?", tableName);
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, document.getAuthor());
            stmt.setString(2, document.getPublisher());
            stmt.setInt(3, document.getPublicationYear());
            stmt.setString(4, document.getTitle());
            
            if (document instanceof ScientificJournal) {
                stmt.setString(5, ((ScientificJournal) document).getResearchField());
            } else if (document instanceof UniversityThesis) {
                stmt.setString(5, ((UniversityThesis) document).getUniversity());
                stmt.setString(6, ((UniversityThesis) document).getField());
            }
            
            stmt.executeUpdate();
        }
    }

    public void delete(String title) {
        String sql = "DELETE FROM library_documents WHERE title = ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Document> findAll() {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM library_documents";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                documents.add(createDocumentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public Optional<Document> findByTitle(String title) {
        String sql = "SELECT * FROM library_documents WHERE title = ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createDocumentFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Document createDocumentFromResultSet(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String title = rs.getString("title");
        String author = rs.getString("author");
        String publisher = rs.getString("publisher");
        int publicationYear = rs.getInt("publication_year");
        String type = getDocumentType(title);

        switch (type) {
            case "Book":
                return new Book(id, title, author, publisher, publicationYear);
            case "Magazine":
                return new Magazine(id, title, author, publisher, publicationYear);
            case "ScientificJournal":
                String researchField = getResearchField(title);
                return new ScientificJournal(id, title, author, publisher, publicationYear, researchField);
            case "UniversityThesis":
                String university = getUniversity(title);
                String field = getField(title);
                return new UniversityThesis(id, title, author, publisher, publicationYear, university, field);
            default:
                throw new IllegalArgumentException("Unknown document type: " + type);
        }
    }

    private String getTableName(Document document) {
        if (document instanceof Book) return "books";
        if (document instanceof Magazine) return "magazines";
        if (document instanceof ScientificJournal) return "scientific_journals";
        if (document instanceof UniversityThesis) return "university_theses";
        throw new IllegalArgumentException("Unknown document type");
    }

    private String getDocumentType(String title) throws SQLException {
        String[] tables = {"books", "magazines", "scientific_journals", "university_theses"};
        for (String table : tables) {
            String sql = String.format("SELECT type FROM %s WHERE title = ?", table);
            try (Connection conn = PostgresConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, title);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("type");
                    }
                }
            }
        }
        throw new SQLException("Document not found in any table");
    }

    private String getResearchField(String title) throws SQLException {
        String sql = "SELECT research_field FROM scientific_journals WHERE title = ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("research_field");
                }
            }
        }
        throw new SQLException("Research field not found for title: " + title);
    }

    private String getUniversity(String title) throws SQLException {
        String sql = "SELECT university FROM university_theses WHERE title = ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("university");
                }
            }
        }
        throw new SQLException("University not found for title: " + title);
    }

    private String getField(String title) throws SQLException {
        String sql = "SELECT field FROM university_theses WHERE title = ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("field");
                }
            }
        }
        throw new SQLException("Field not found for title: " + title);
    }
}