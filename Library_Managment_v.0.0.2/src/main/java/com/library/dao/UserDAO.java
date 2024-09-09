package com.library.dao;

import com.library.model.user.User;
import com.library.model.user.Student;
import com.library.model.user.Professor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    private static volatile UserDAO instance;

    private UserDAO() {
        // private constructor to prevent instantiation
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            synchronized (UserDAO.class) {
                if (instance == null) {
                    instance = new UserDAO();
                }
            }
        }
        return instance;
    }

    public void save(User user) {
        LOGGER.info("Attempting to save user: " + user.getName());
        
        Connection conn = null;
        try {
            conn = PostgresConnection.getConnection();
            conn.setAutoCommit(false);

            // Insert into library_users first
            String sqlLibraryUsers = "INSERT INTO library_users (id, name, email, phone_number) VALUES (?, ?, ?, ?)";
            UUID id = UUID.randomUUID();
            try (PreparedStatement stmt = conn.prepareStatement(sqlLibraryUsers)) {
                stmt.setObject(1, id);
                stmt.setString(2, user.getName());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getPhoneNumber());
                stmt.executeUpdate();
            }

            // Then insert into the specific user type table
            String sqlSpecificUser;
            if (user instanceof Student) {
                sqlSpecificUser = "INSERT INTO student_users (id, name, email, phone_number, student_id, department) VALUES (?, ?, ?, ?, ?, ?)";
            } else if (user instanceof Professor) {
                sqlSpecificUser = "INSERT INTO professor_users (id, name, email, phone_number, department) VALUES (?, ?, ?, ?, ?)";
            } else {
                throw new IllegalArgumentException("Unknown user type");
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlSpecificUser)) {
                stmt.setObject(1, id);
                stmt.setString(2, user.getName());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getPhoneNumber());
                if (user instanceof Student) {
                    Student student = (Student) user;
                    stmt.setString(5, student.getStudentId());
                    stmt.setString(6, student.getDepartment());
                } else if (user instanceof Professor) {
                    Professor professor = (Professor) user;
                    stmt.setString(5, professor.getDepartment());
                }
                stmt.executeUpdate();
            }

            conn.commit();
            LOGGER.info("User saved successfully: " + user.getName());
        } catch (SQLException e) {
            LOGGER.severe("Error saving user: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.severe("Error rolling back transaction: " + ex.getMessage());
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.severe("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    public void update(User user) {
        Connection conn = null;
        try {
            conn = PostgresConnection.getConnection();
            conn.setAutoCommit(false);

            // Update library_users
            String sqlLibraryUsers = "UPDATE library_users SET email = ?, phone_number = ? WHERE name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlLibraryUsers)) {
                stmt.setString(1, user.getEmail());
                stmt.setString(2, user.getPhoneNumber());
                stmt.setString(3, user.getName());
                stmt.executeUpdate();
            }

            // Update specific user table
            String sqlSpecificUser;
            if (user instanceof Student) {
                sqlSpecificUser = "UPDATE student_users SET email = ?, phone_number = ?, student_id = ?, department = ? WHERE name = ?";
            } else if (user instanceof Professor) {
                sqlSpecificUser = "UPDATE professor_users SET email = ?, phone_number = ?, department = ? WHERE name = ?";
            } else {
                throw new IllegalArgumentException("Unknown user type");
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlSpecificUser)) {
                stmt.setString(1, user.getEmail());
                stmt.setString(2, user.getPhoneNumber());
                if (user instanceof Student) {
                    Student student = (Student) user;
                    stmt.setString(3, student.getStudentId());
                    stmt.setString(4, student.getDepartment());
                    stmt.setString(5, student.getName());
                } else if (user instanceof Professor) {
                    Professor professor = (Professor) user;
                    stmt.setString(3, professor.getDepartment());
                    stmt.setString(4, professor.getName());
                }
                stmt.executeUpdate();
            }

            conn.commit();
            LOGGER.info("User updated successfully: " + user.getName());
        } catch (SQLException e) {
            LOGGER.severe("Error updating user: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.severe("Error rolling back transaction: " + ex.getMessage());
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.severe("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    public void delete(String name) {
        Connection conn = null;
        try {
            conn = PostgresConnection.getConnection();
            conn.setAutoCommit(false);

            String[] tables = {"library_users", "student_users", "professor_users"};
            for (String table : tables) {
                String sql = "DELETE FROM " + table + " WHERE name = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, name);
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            LOGGER.info("User deleted successfully: " + name);
        } catch (SQLException e) {
            LOGGER.severe("Error deleting user: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.severe("Error rolling back transaction: " + ex.getMessage());
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.severe("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String[] tables = { "student_users", "professor_users" };
        for (String table : tables) {
            String sql = "SELECT * FROM " + table;
            try (Connection conn = PostgresConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(createUserFromResultSet(rs, table));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    public Optional<User> findByName(String name) {
        String[] tables = { "student_users", "professor_users" };
        for (String table : tables) {
            String sql = "SELECT * FROM " + table + " WHERE name = ?";
            try (Connection conn = PostgresConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(createUserFromResultSet(rs, table));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }
    public boolean userExists(String name) {
        String sql = "SELECT COUNT(*) FROM library_users WHERE name = ?";
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error checking user existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private User createUserFromResultSet(ResultSet rs, String table) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String phoneNumber = rs.getString("phone_number");
        String department = rs.getString("department");

        if ("student_users".equals(table)) {
            String studentId = rs.getString("student_id");
            return new Student(id, name, email, phoneNumber, studentId, department);
        } else {
            return new Professor(id, name, email, phoneNumber, department);
        }
    }
}