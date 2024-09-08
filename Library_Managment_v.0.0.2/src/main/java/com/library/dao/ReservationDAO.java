package com.library.dao;

import com.library.model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReservationDAO {
    private static ReservationDAO instance;

    private ReservationDAO() {
        // private constructor to prevent instantiation
    }

    public static ReservationDAO getInstance() {
        if (instance == null) {
            instance = new ReservationDAO();
        }
        return instance;
    }

    public void save(Reservation reservation) {
        String sql = "INSERT INTO reservations (id, document_id, user_id, reservation_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, reservation.getId());
            stmt.setObject(2, reservation.getDocumentId());
            stmt.setObject(3, reservation.getUserId());
            stmt.setDate(4, Date.valueOf(reservation.getReservationDate()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Reservation> findAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Reservation reservation = createReservation(rs);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public void delete(UUID reservationId) {
        String sql = "DELETE FROM reservations WHERE id = ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, reservationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Reservation createReservation(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        UUID documentId = (UUID) rs.getObject("document_id");
        UUID userId = (UUID) rs.getObject("user_id");
        Date reservationDate = rs.getDate("reservation_date");
        return new Reservation(id, documentId, userId, reservationDate.toLocalDate());
    }
}