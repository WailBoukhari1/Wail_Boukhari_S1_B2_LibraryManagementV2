package org.library.service;

import org.library.dao.DocumentDAO;
import org.library.dao.UserDAO;
import org.library.model.Document;
import org.library.model.User;

import java.util.List;
import java.util.Optional;

public class Library {
    private final UserDAO userDAO;
    private final DocumentDAO documentDAO;

    public Library(UserDAO userDAO, DocumentDAO documentDAO) {
        this.userDAO = userDAO;
        this.documentDAO = documentDAO;
    }

    // User management methods
    public User addUser(User user) {
        return userDAO.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userDAO.findById(id);
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public void updateUser(User user) {
        userDAO.update(user);
    }

    public void deleteUser(Long id) {
        userDAO.delete(id);
    }

    // Document management methods
    public Document addDocument(Document document) {
        return documentDAO.save(document);
    }

    public Optional<Document> getDocumentById(Long id) {
        return documentDAO.findById(id);
    }

    public List<Document> getAllDocuments() {
        return documentDAO.findAll();
    }

    public void updateDocument(Document document) {
        documentDAO.update(document);
    }

    public void deleteDocument(Long id) {
        documentDAO.delete(id);
    }

    // Borrowing and reserving methods
    public void borrowDocument(Long userId, Long documentId) {
        Optional<User> user = userDAO.findById(userId);
        Optional<Document> document = documentDAO.findById(documentId);

        if (user.isPresent() && document.isPresent()) {
            Document doc = document.get();
            if (doc instanceof Borrowable) {
                ((Borrowable) doc).borrow();
                documentDAO.update(doc);
                System.out.println("Document borrowed successfully.");
            } else {
                System.out.println("This document cannot be borrowed.");
            }
        } else {
            System.out.println("User or document not found.");
        }
    }

    public void returnDocument(Long userId, Long documentId) {
        Optional<User> user = userDAO.findById(userId);
        Optional<Document> document = documentDAO.findById(documentId);

        if (user.isPresent() && document.isPresent()) {
            Document doc = document.get();
            if (doc instanceof Borrowable) {
                ((Borrowable) doc).returnItem();
                documentDAO.update(doc);
                System.out.println("Document returned successfully.");
            } else {
                System.out.println("This document cannot be returned.");
            }
        } else {
            System.out.println("User or document not found.");
        }
    }

    public void reserveDocument(Long userId, Long documentId) {
        Optional<User> user = userDAO.findById(userId);
        Optional<Document> document = documentDAO.findById(documentId);

        if (user.isPresent() && document.isPresent()) {
            Document doc = document.get();
            if (doc instanceof Reservable) {
                ((Reservable) doc).reserve();
                documentDAO.update(doc);
                System.out.println("Document reserved successfully.");
            } else {
                System.out.println("This document cannot be reserved.");
            }
        } else {
            System.out.println("User or document not found.");
        }
    }

    public void cancelReservation(Long userId, Long documentId) {
        Optional<User> user = userDAO.findById(userId);
        Optional<Document> document = documentDAO.findById(documentId);

        if (user.isPresent() && document.isPresent()) {
            Document doc = document.get();
            if (doc instanceof Reservable) {
                ((Reservable) doc).cancelReservation();
                documentDAO.update(doc);
                System.out.println("Reservation canceled successfully.");
            } else {
                System.out.println("This document cannot have reservations canceled.");
            }
        } else {
            System.out.println("User or document not found.");
        }
    }
}