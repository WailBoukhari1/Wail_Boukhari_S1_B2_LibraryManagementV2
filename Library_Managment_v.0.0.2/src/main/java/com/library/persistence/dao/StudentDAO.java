package com.library.persistence.dao;

import com.library.business.user.Student;

import java.sql.*;
import java.util.List;

public class StudentDAO extends UserDAO<Student> {

    @Override
    public Student findById(String id) {
        List<Student> results = executeQuery("SELECT * FROM students WHERE id = ?", id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Student> findAll() {
        return executeQuery("SELECT * FROM students");
    }

    @Override
    public void save(Student student) {
        String sql = "INSERT INTO students (id, name, email, student_id, major) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getId());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getStudentId());
            stmt.setString(5, student.getMajor());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Student student) {
        String sql = "UPDATE students SET name = ?, email = ?, student_id = ?, major = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getStudentId());
            stmt.setString(4, student.getMajor());
            stmt.setString(5, student.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM students WHERE id = ?")) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Student createFromResultSet(ResultSet rs) throws SQLException {
        return new Student(
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("student_id"),
            rs.getString("major")
        );
    }
}
