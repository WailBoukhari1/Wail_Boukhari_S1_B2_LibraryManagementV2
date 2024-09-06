package com.library.business;

import com.library.business.document.*;
import com.library.business.user.*;
import com.library.persistence.dao.*;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Library {
    private BookDAO bookDAO;
    private MagazineDAO magazineDAO;
    private ScientificJournalDAO scientificJournalDAO;
    private UniversityThesisDAO universityThesisDAO;
    private StudentDAO studentDAO;
    private ProfessorDAO professorDAO;

    public Library() {
        this.bookDAO = new BookDAO();
        this.magazineDAO = new MagazineDAO();
        this.scientificJournalDAO = new ScientificJournalDAO();
        this.universityThesisDAO = new UniversityThesisDAO();
        this.studentDAO = new StudentDAO();
        this.professorDAO = new ProfessorDAO();
    }

    // Document management methods
    public void addDocument(Document document) {
        if (document instanceof Book) {
            bookDAO.save((Book) document);
        } else if (document instanceof Magazine) {
            magazineDAO.save((Magazine) document);
        } else if (document instanceof ScientificJournal) {
            scientificJournalDAO.save((ScientificJournal) document);
        } else if (document instanceof UniversityThesis) {
            universityThesisDAO.save((UniversityThesis) document);
        }
    }

    public Document findDocument(String id) {
        Document document = bookDAO.findById(id);
        if (document == null) document = magazineDAO.findById(id);
        if (document == null) document = scientificJournalDAO.findById(id);
        if (document == null) document = universityThesisDAO.findById(id);
        return document;
    }

    // User management methods
    public void addUser(User user) {
        if (user instanceof Student) {
            studentDAO.save((Student) user);
        } else if (user instanceof Professor) {
            professorDAO.save((Professor) user);
        }
    }

    public User findUser(String id) {
        User user = studentDAO.findById(id);
        if (user == null) user = professorDAO.findById(id);
        return user;
    }

    // Borrowing and reservation methods
    public void borrowDocument(String userId, String documentId) {
        User user = findUser(userId);
        Document document = findDocument(documentId);
        if (user != null && document != null && document instanceof Borrowable) {
            ((Borrowable) document).borrow(userId);
            // Update the document in the database
            addDocument(document);
        }
    }

    public void returnDocument(String documentId) {
        Document document = findDocument(documentId);
        if (document != null && document instanceof Borrowable) {
            ((Borrowable) document).returnItem();
            // Update the document in the database
            addDocument(document);
        }
    }

    public void reserveDocument(String userId, String documentId) {
        User user = findUser(userId);
        Document document = findDocument(documentId);
        if (user != null && document != null && document instanceof Reservable) {
            ((Reservable) document).reserve(userId);
            // Update the document in the database
            addDocument(document);
        }
    }

    public void cancelReservation(String documentId) {
        Document document = findDocument(documentId);
        if (document != null && document instanceof Reservable) {
            ((Reservable) document).cancelReservation();
            // Update the document in the database
            addDocument(document);
        }
    }

    public List<Document> searchDocuments(String keyword) {
        List<Document> results = new ArrayList<>();
        results.addAll(bookDAO.findAll().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        book.getAuthor().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList()));
        // Add similar searches for other document types
        return results;
    }
}
