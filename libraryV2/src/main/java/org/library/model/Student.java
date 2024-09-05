package org.library.model;

import java.util.Objects;

public class Student extends User {
    private String studentId;
    private String major;

    // Constructor with parameters
    public Student(String name, String email, String studentId, String major) {
        super(name, email, "Student");
        this.studentId = studentId;
        this.major = major;
    }

    // Getters and setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    // Override equals and hashCode for better comparison and hashing
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(studentId, student.studentId) &&
                Objects.equals(major, student.major);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), studentId, major);
    }

    // Override toString for better readability
    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", major='" + major + '\'' +
                "} " + super.toString();
    }
}
