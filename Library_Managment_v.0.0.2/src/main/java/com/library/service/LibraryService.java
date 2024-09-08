package com.library.service;

import com.library.dao.DocumentDAO;
import com.library.model.document.Document;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LibraryService {
    private final DocumentDAO documentDAO;

    public LibraryService() {
        this.documentDAO = DocumentDAO.getInstance();
    }

    public void addDocument(Document document) {
        documentDAO.save(document);
    }

    public List<Document> getAllDocuments() {
        return documentDAO.findAll();
    }

    public Optional<Document> findDocumentByTitle(String title) {
        return documentDAO.findAll().stream()
                .filter(doc -> doc.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }

    public Optional<Document> findDocumentById(UUID id) {
        return documentDAO.findAll().stream()
                .filter(doc -> doc.getId().equals(id))
                .findFirst();
    }

    public void updateDocument(Document document) {
        documentDAO.update(document);
    }

    public void deleteDocument(UUID documentId) {
        documentDAO.delete(documentId);
    }
}