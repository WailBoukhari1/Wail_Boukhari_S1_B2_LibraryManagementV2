package org.library.model;

public class Student extends User {
    private String studentId;
    private String major;

    public Student() {
        super();
        setUserType("Student");
    }

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

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", major='" + major + '\'' +
                "} " + super.toString();
    }
}
