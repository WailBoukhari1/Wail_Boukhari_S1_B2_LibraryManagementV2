package com.library.persistence.dao;

import com.library.business.user.User;
import com.library.persistence.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class UserDAO<T extends User> {
    protected Connection connection;

    public UserDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public abstract T findById(String id);
    public abstract List<T> findAll();
    public abstract void save(T user);
    public abstract void update(T user);
    public abstract void delete(String id);

    protected List<T> executeQuery(String sql, Object... params) {
        List<T> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(createFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    protected abstract T createFromResultSet(ResultSet rs) throws SQLException;
}
