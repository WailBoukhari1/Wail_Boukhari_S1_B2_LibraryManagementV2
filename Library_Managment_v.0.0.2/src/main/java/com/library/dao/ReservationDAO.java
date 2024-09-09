package com.library.dao;

import com.library.model.Reservation;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        String sql = "INSERT INTO reservations (id, document_title, user_name, reservation_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, UUID.randomUUID());
            stmt.setString(2, reservation.getDocumentTitle());
            stmt.setString(3, reservation.getUserName());
            stmt.setDate(4, java.sql.Date.valueOf(reservation.getReservationDate()));
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
                Reservation reservation = createReservationFromResultSet(rs);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public void delete(String documentTitle, String userName) {
        String sql = "DELETE FROM reservations WHERE document_title = ? AND user_name = ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, documentTitle);
            stmt.setString(2, userName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Reservation> findByDocumentAndUser(String documentTitle, String userName) {
        String sql = "SELECT * FROM reservations WHERE document_title = ? AND user_name = ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, documentTitle);
            stmt.setString(2, userName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createReservationFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Reservation> findByDocument(String documentTitle) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE document_title = ? ORDER BY reservation_date ASC";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, documentTitle);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(createReservationFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    private Reservation createReservationFromResultSet(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String documentTitle = rs.getString("document_title");
        String userName = rs.getString("user_name");
        LocalDate reservationDate = rs.getDate("reservation_date").toLocalDate();

        return new Reservation(id, documentTitle, userName, reservationDate);
    }
}