package com.library.service;

import com.library.dao.LoanDAO;
import com.library.model.Loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class LoanService {
    private final LoanDAO loanDAO;
    private final ReservationService reservationService;

    public LoanService(ReservationService reservationService) {
        this.loanDAO = LoanDAO.getInstance();
        this.reservationService = reservationService;
    }

    public void borrowDocument(Loan loan) {
        loanDAO.save(loan);
        reservationService.cancelReservationByUserAndDocument(loan.getUserId(), loan.getDocumentId());
    }

    public void returnDocument(UUID loanId, LocalDate returnDate) {
        Optional<Loan> loanOpt = findLoanById(loanId);
        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            loan.setReturnDate(returnDate);
            loanDAO.update(loan);
        }
    }

    public List<Loan> getAllLoans() {
        return loanDAO.findAll();
    }

    public Optional<Loan> findLoanById(UUID id) {
        return loanDAO.findAll().stream()
                .filter(loan -> loan.getId().equals(id))
                .findFirst();
    }

    public List<Loan> findLoansByUserId(UUID userId) {
        return loanDAO.findAll().stream()
                .filter(loan -> loan.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public Optional<Loan> findLoanByUserAndDocument(UUID userId, UUID documentId) {
        return loanDAO.findAll().stream()
                .filter(loan -> loan.getUserId().equals(userId) && loan.getDocumentId().equals(documentId))
                .findFirst();
    }

    public boolean isDocumentLoaned(UUID documentId) {
        return loanDAO.findAll().stream()
                .anyMatch(loan -> loan.getDocumentId().equals(documentId) && loan.getReturnDate() == null);
    }
}