package org.library.dao;

import org.library.model.User;
import org.library.db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;

public class UserDAO implements GenericDAO<User, Long> {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());
    private static final String SELECT_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM users";
    private static final String INSERT = "INSERT INTO users (name, email, user_type) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE users SET name = ?, email = ?, user_type = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM users WHERE id = ?";

    @Override
    public Optional<User> findById(Long id) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createUserFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding user by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {
            while (rs.next()) {
                users.add(createUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding all users", e);
        }
        return users;
    }

    @Override
    public User save(User user) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            setUserParameters(stmt, user);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving user", e);
        }
        return user;
    }

    @Override
    public void update(User user) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            setUserParameters(stmt, user);
            stmt.setLong(4, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting user", e);
        }
    }

    private User createUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User(); // Make sure User has a public no-arg constructor
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setUserType(rs.getString("user_type"));
        return user;
    }

    private void setUserParameters(PreparedStatement stmt, User user) throws SQLException {
        stmt.setString(1, user.getName());
        stmt.setString(2, user.getEmail());
        stmt.setString(3, user.getUserType());
    }
}
