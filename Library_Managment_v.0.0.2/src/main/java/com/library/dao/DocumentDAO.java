package com.library.dao;

import com.library.model.document.Document;
import com.library.model.document.Book;
import com.library.model.document.Magazine;
import com.library.model.document.ScientificJournal;
import com.library.model.document.UniversityThesis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DocumentDAO {
    private static DocumentDAO instance;
    public static DocumentDAO getInstance() {
        if (instance == null) {
            instance = new DocumentDAO();
        }
        return instance;
    }

    public void save(Document document) {
        String sql = "INSERT INTO documents (id, title, type, author, publisher, publication_year, research_field, university, field) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, document.getId());
            stmt.setString(2, document.getTitle());
            stmt.setString(3, document.getType());
            stmt.setString(4, document.getAuthor());
            stmt.setString(5, document.getPublisher());
            stmt.setInt(6, document.getPublicationYear());
            if (document instanceof Book) {
                stmt.setString(7, null);
                stmt.setString(8, null);
                stmt.setString(9, null);
            } else if (document instanceof Magazine) {
                stmt.setString(7, null);
                stmt.setString(8, null);
                stmt.setString(9, null);
            } else if (document instanceof ScientificJournal) {
                stmt.setString(7, ((ScientificJournal) document).getResearchField());
                stmt.setString(8, null);
                stmt.setString(9, null);
            } else if (document instanceof UniversityThesis) {
                stmt.setString(7, null);
                stmt.setString(8, ((UniversityThesis) document).getUniversity());
                stmt.setString(9, ((UniversityThesis) document).getField());
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Document> findAll() {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM documents";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Document document = createDocument(rs);
                documents.add(document);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public List<Document> findByTitle(String title) {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM documents WHERE title ILIKE ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + title + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Document document = createDocument(rs);
                    documents.add(document);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public void update(Document document) {
        String sql = "UPDATE documents SET title = ?, author = ?, publisher = ?, publication_year = ?, research_field = ?, university = ?, field = ? WHERE id = ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, document.getTitle());
            stmt.setString(2, document.getAuthor());
            stmt.setString(3, document.getPublisher());
            stmt.setInt(4, document.getPublicationYear());
            if (document instanceof Book) {
                stmt.setString(5, null);
                stmt.setString(6, null);
                stmt.setString(7, null);
            } else if (document instanceof Magazine) {
                stmt.setString(5, null);
                stmt.setString(6, null);
                stmt.setString(7, null);
            } else if (document instanceof ScientificJournal) {
                stmt.setString(5, ((ScientificJournal) document).getResearchField());
                stmt.setString(6, null);
                stmt.setString(7, null);
            } else if (document instanceof UniversityThesis) {
                stmt.setString(5, null);
                stmt.setString(6, ((UniversityThesis) document).getUniversity());
                stmt.setString(7, ((UniversityThesis) document).getField());
            }
            stmt.setObject(8, document.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(UUID documentId) {
        String sql = "DELETE FROM documents WHERE id = ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, documentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Document createDocument(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String title = rs.getString("title");
        String author = rs.getString("author");
        String publisher = rs.getString("publisher");
        int publicationYear = rs.getInt("publication_year");
        String type = rs.getString("type");
        switch (type) {
            case "Book":
                return new Book(id, title, author, publisher, publicationYear);
            case "Magazine":
                return new Magazine(id, title, author, publisher, publicationYear);
            case "ScientificJournal":
                return new ScientificJournal(id, title, author, publisher, publicationYear, rs.getString("research_field"));
            case "UniversityThesis":
                return new UniversityThesis(id, title, author, publisher, publicationYear, rs.getString("university"), rs.getString("field"));
            default:
                throw new IllegalArgumentException("Unknown document type: " + type);
        }
    }
}