package com.library.service;

import com.library.dao.LoanDAO;
import com.library.dao.UserDAO;
import com.library.dao.DocumentDAO;
import com.library.model.Loan;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LoanService {
    private final LoanDAO loanDAO;
    private final UserDAO userDAO;
    private final DocumentDAO documentDAO;

    public LoanService() {
        this.loanDAO = LoanDAO.getInstance();
        this.userDAO = UserDAO.getInstance();
        this.documentDAO = DocumentDAO.getInstance();
    }

    public void loanDocument(String documentTitle, String userName) {
        if (!userDAO.userExists(userName)) {
            throw new IllegalArgumentException("User does not exist: " + userName);
        }
        if (!documentDAO.documentExists(documentTitle)) {
            throw new IllegalArgumentException("Document does not exist: " + documentTitle);
        }
        if (isDocumentLoaned(documentTitle)) {
            throw new IllegalArgumentException("Document is already loaned: " + documentTitle);
        }
        Loan loan = new Loan(null, documentTitle, userName, LocalDate.now(), null);
        try {
            loanDAO.save(loan);
        } catch (Exception e) {
            throw new RuntimeException("Unable to process loan. Please try again later.");
        }
    }

    public void returnDocument(String documentTitle, String userName) {
        Optional<Loan> loanOpt = loanDAO.findByDocumentAndUser(documentTitle, userName);
        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            loan.setReturnDate(LocalDate.now());
            try {
                loanDAO.update(loan);
            } catch (Exception e) {
                throw new RuntimeException("Unable to process return. Please try again later.");
            }
        } else {
            throw new IllegalArgumentException("No active loan found for this document and user.");
        }
    }

    public List<Loan> getAllLoans() {
        try {
            return loanDAO.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Unable to retrieve loans. Please try again later.");
        }
    }

    public boolean isDocumentLoaned(String documentTitle) {
        try {
            return loanDAO.findAll().stream()
                    .anyMatch(loan -> loan.getDocumentTitle().equals(documentTitle) && loan.getReturnDate() == null);
        } catch (Exception e) {
            throw new RuntimeException("Unable to check document loan status. Please try again later.");
        }
    }
}