package com.library.persistence.dao;

import com.library.business.user.Professor;

import java.sql.*;
import java.util.List;

public class ProfessorDAO extends UserDAO<Professor> {

    @Override
    public Professor findById(String id) {
        List<Professor> results = executeQuery("SELECT * FROM professors WHERE id = ?", id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Professor> findAll() {
        return executeQuery("SELECT * FROM professors");
    }

    @Override
    public void save(Professor professor) {
        String sql = "INSERT INTO professors (id, name, email, department, research_area) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, professor.getId());
            stmt.setString(2, professor.getName());
            stmt.setString(3, professor.getEmail());
            stmt.setString(4, professor.getDepartment());
            stmt.setString(5, professor.getResearchArea());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Professor professor) {
        String sql = "UPDATE professors SET name = ?, email = ?, department = ?, research_area = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, professor.getName());
            stmt.setString(2, professor.getEmail());
            stmt.setString(3, professor.getDepartment());
            stmt.setString(4, professor.getResearchArea());
            stmt.setString(5, professor.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM professors WHERE id = ?")) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Professor createFromResultSet(ResultSet rs) throws SQLException {
        return new Professor(
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("department"),
            rs.getString("research_area")
        );
    }
}
