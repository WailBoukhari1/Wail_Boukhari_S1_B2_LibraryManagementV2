package com.library.service;

import com.library.dao.ReservationDAO;
import com.library.model.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ReservationService {
    private final ReservationDAO reservationDAO;
    private LoanService loanService;

    public ReservationService() {
        this.reservationDAO = ReservationDAO.getInstance();
        this.loanService = null; // We'll set this later
    }

    public void setLoanService(LoanService loanService) {
        this.loanService = loanService;
    }

    public boolean reserveDocument(Reservation reservation) {
        if (loanService.isDocumentLoaned(reservation.getDocumentId())) {
            List<Reservation> existingReservations = findReservationsByDocumentId(reservation.getDocumentId());
            if (existingReservations.stream().anyMatch(r -> r.getUserId().equals(reservation.getUserId()))) {
                return false; // User already has a reservation for this document
            }
            reservationDAO.save(reservation);
            return true;
        }
        return false; // Document is not loaned, so it can't be reserved
    }

    public boolean cancelReservation(UUID reservationId) {
        Optional<Reservation> reservation = findReservationById(reservationId);
        if (reservation.isPresent()) {
            reservationDAO.delete(reservationId);
            return true;
        }
        return false;
    }

    public List<Reservation> getAllReservations() {
        return reservationDAO.findAll();
    }

    public Optional<Reservation> findReservationById(UUID id) {
        return reservationDAO.findAll().stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst();
    }

    public List<Reservation> findReservationsByUserId(UUID userId) {
        return reservationDAO.findAll().stream()
                .filter(reservation -> reservation.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Reservation> findReservationsByDocumentId(UUID documentId) {
        return reservationDAO.findAll().stream()
                .filter(reservation -> reservation.getDocumentId().equals(documentId))
                .sorted((r1, r2) -> r1.getReservationDate().compareTo(r2.getReservationDate()))
                .collect(Collectors.toList());
    }

    public void removeExpiredReservations() {
        LocalDate expirationDate = LocalDate.now().minusDays(7); // Reservations expire after 7 days
        reservationDAO.findAll().stream()
                .filter(reservation -> reservation.getReservationDate().isBefore(expirationDate))
                .forEach(reservation -> reservationDAO.delete(reservation.getId()));
    }

    public Optional<Reservation> getNextReservation(UUID documentId) {
        return findReservationsByDocumentId(documentId).stream().findFirst();
    }

    public List<UUID> getUsersReservingDocument(UUID documentId) {
        return findReservationsByDocumentId(documentId).stream()
                .map(Reservation::getUserId)
                .collect(Collectors.toList());
    }

    public void cancelReservationByUserAndDocument(UUID userId, UUID documentId) {
        findReservationsByDocumentId(documentId).stream()
                .filter(r -> r.getUserId().equals(userId))
                .findFirst()
                .ifPresent(r -> cancelReservation(r.getId()));
    }
}