package com.library.dao;

import com.library.model.user.User;
import com.library.model.user.Student;
import com.library.model.user.Professor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDAO {
    private static UserDAO instance;
    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    public void save(User user) {
        String sql = "INSERT INTO users (id, name, type, email, phone_number, student_id, department) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, user.getId());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getType());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhoneNumber());
            if (user instanceof Student) {
                stmt.setString(6, ((Student) user).getStudentId());
                stmt.setString(7, null);
            } else if (user instanceof Professor) {
                stmt.setString(6, null);
                stmt.setString(7, ((Professor) user).getDepartment());
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = createUser(rs);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<User> findByName(String name) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE name ILIKE ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = createUser(rs);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void update(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, phone_number = ?, student_id = ?, department = ? WHERE id = ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhoneNumber());
            if (user instanceof Student) {
                stmt.setString(4, ((Student) user).getStudentId());
                stmt.setString(5, null);
            } else if (user instanceof Professor) {
                stmt.setString(4, null);
                stmt.setString(5, ((Professor) user).getDepartment());
            }
            stmt.setObject(6, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(UUID userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User createUser(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String phoneNumber = rs.getString("phone_number");
        String type = rs.getString("type");
        switch (type) {
            case "Student":
                return new Student(id, name, email, phoneNumber, rs.getString("student_id"));
            case "Professor":
                return new Professor(id, name, email, phoneNumber, rs.getString("department"));
            default:
                throw new IllegalArgumentException("Unknown user type: " + type);
        }
    }
}