package com.library.dao;

import com.library.model.Loan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoanDAO {
    private static LoanDAO instance;

    private LoanDAO() {
        // private constructor to prevent instantiation
    }

    public static LoanDAO getInstance() {
        if (instance == null) {
            instance = new LoanDAO();
        }
        return instance;
    }

    public void save(Loan loan) {
        String sql = "INSERT INTO loans (id, document_id, user_id, loan_date, return_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, loan.getId());
            stmt.setObject(2, loan.getDocumentId());
            stmt.setObject(3, loan.getUserId());
            stmt.setDate(4, Date.valueOf(loan.getLoanDate()));
            stmt.setDate(5, loan.getReturnDate() != null ? Date.valueOf(loan.getReturnDate()) : null);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Loan> findAll() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Loan loan = createLoan(rs);
                loans.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    public void delete(UUID loanId) {
        String sql = "DELETE FROM loans WHERE id = ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, loanId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void update(Loan loan) {
        String sql = "UPDATE loans SET document_id = ?, user_id = ?, loan_date = ?, return_date = ? WHERE id = ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, loan.getDocumentId());
            stmt.setObject(2, loan.getUserId());
            stmt.setDate(3, Date.valueOf(loan.getLoanDate()));
            stmt.setDate(4, loan.getReturnDate() != null ? Date.valueOf(loan.getReturnDate()) : null);
            stmt.setObject(5, loan.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Loan createLoan(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        UUID documentId = (UUID) rs.getObject("document_id");
        UUID userId = (UUID) rs.getObject("user_id");
        Date loanDate = rs.getDate("loan_date");
        Date returnDate = rs.getDate("return_date");
        return new Loan(id, documentId, userId, loanDate.toLocalDate(), returnDate != null ? returnDate.toLocalDate() : null);
    }
}