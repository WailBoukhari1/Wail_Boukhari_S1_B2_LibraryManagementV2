package com.library.model.user;

import java.util.UUID;

public class Student extends User {
    private String studentId;

    public Student(UUID id, String name, String email, String phoneNumber, String studentId) {
        super(id, name, email, phoneNumber);
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public String getType() {
        return "Student";
    }
}