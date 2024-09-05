package org.library.dao;

import org.library.db.DatabaseConnection;
import org.library.model.Document;
import static org.library.model.Document.createDocument;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentDAO implements GenericDAO<Document, Long> {
    private static final Logger LOGGER = Logger.getLogger(DocumentDAO.class.getName());
    private static final String SELECT_BY_ID = "SELECT * FROM documents WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM documents";
    private static final String INSERT = "INSERT INTO documents (title, author, publication_date, type) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE documents SET title = ?, author = ?, publication_date = ?, type = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM documents WHERE id = ?";

    @Override
    public Optional<Document> findById(Long id) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createDocumentFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding document by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Document> findAll() {
        List<Document> documents = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {
            while (rs.next()) {
                documents.add(createDocumentFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding all documents", e);
        }
        return documents;
    }

    @Override
    public Document save(Document document) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            setDocumentParameters(stmt, document);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating document failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    document.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating document failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving document", e);
        }
        return document;
    }

    @Override
    public void update(Document document) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            setDocumentParameters(stmt, document);
            stmt.setLong(5, document.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating document", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting document", e);
        }
    }

    private Document createDocumentFromResultSet(ResultSet rs) throws SQLException {
        return createDocument(
            rs.getString("title"),
            rs.getString("author"),
            rs.getDate("publication_date").toLocalDate(),
            rs.getString("type")
        );
    }

    private void setDocumentParameters(PreparedStatement stmt, Document document) throws SQLException {
        stmt.setString(1, document.getTitle());
        stmt.setString(2, document.getAuthor());
        stmt.setDate(3, Date.valueOf(document.getPublicationDate()));
        stmt.setString(4, document.getType());
    }
}
