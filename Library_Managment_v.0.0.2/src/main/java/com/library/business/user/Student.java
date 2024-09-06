package com.library.business.user;

public class Student extends User {
    private String studentId;
    private String major;

    public Student(String id, String name, String email, String studentId, String major) {
        super(id, name, email);
        this.studentId = studentId;
        this.major = major;
    }

    // Getters and setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    @Override
    public int getMaxBorrowDays() {
        return 14; // Students can borrow for 14 days
    }
}
