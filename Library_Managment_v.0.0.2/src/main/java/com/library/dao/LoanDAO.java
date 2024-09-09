package com.library.dao;

import com.library.model.Loan;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LoanDAO {
    private static LoanDAO instance;

    private LoanDAO() {}

    public static LoanDAO getInstance() {
        if (instance == null) {
            instance = new LoanDAO();
        }
        return instance;
    }

    public void save(Loan loan) {
        String sql = "INSERT INTO loans (id, document_title, user_name, loan_date, return_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, UUID.randomUUID());
            stmt.setString(2, loan.getDocumentTitle());
            stmt.setString(3, loan.getUserName());
            stmt.setDate(4, java.sql.Date.valueOf(loan.getLoanDate()));
            stmt.setDate(5, loan.getReturnDate() != null ? java.sql.Date.valueOf(loan.getReturnDate()) : null);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Loan loan) {
        String sql = "UPDATE loans SET return_date = ? WHERE document_title = ? AND user_name = ? AND loan_date = ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, loan.getReturnDate() != null ? java.sql.Date.valueOf(loan.getReturnDate()) : null);
            stmt.setString(2, loan.getDocumentTitle());
            stmt.setString(3, loan.getUserName());
            stmt.setDate(4, java.sql.Date.valueOf(loan.getLoanDate()));
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
                loans.add(createLoanFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    public Optional<Loan> findByDocumentAndUser(String documentTitle, String userName) {
        String sql = "SELECT * FROM loans WHERE document_title = ? AND user_name = ? AND return_date IS NULL";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, documentTitle);
            stmt.setString(2, userName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createLoanFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Loan createLoanFromResultSet(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String documentTitle = rs.getString("document_title");
        String userName = rs.getString("user_name");
        LocalDate loanDate = rs.getDate("loan_date").toLocalDate();
        Date returnDateSql = rs.getDate("return_date");
        LocalDate returnDate = returnDateSql != null ? returnDateSql.toLocalDate() : null;

        return new Loan(id, documentTitle, userName, loanDate, returnDate);
    }
}