package org.library.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private String url;
    private String user;
    private String password;

    private DatabaseConnection() {
        loadProperties();
        loadDriver();
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing the database connection: " + e.getMessage());
        }
    }

    // Test methods
    public void testLoadProperties() {
        loadProperties();
        if (url == null || user == null || password == null) {
            throw new RuntimeException("Failed to load database properties");
        }
    }

    public void testLoadDriver() {
        loadDriver();
    }

    public void testConnection() throws SQLException {
        try (Connection conn = getConnection()) {
            if (!conn.isValid(5)) {
                throw new SQLException("Failed to establish a valid connection");
            }
        }
    }

    // Helper methods
    private void loadProperties() {
        try {
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream("src/main/resources/application.properties")) {
                props.load(fis);
            }
            
            this.url = props.getProperty("db.url");
            this.user = props.getProperty("db.user");
            this.password = props.getProperty("db.password");
            
            if (this.url == null || this.user == null || this.password == null) {
                throw new RuntimeException("Missing database properties");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database properties", e);
        }
    }

    private void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load PostgreSQL JDBC driver", e);
        }
    }
}
