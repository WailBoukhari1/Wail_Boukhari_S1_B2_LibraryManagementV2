package org.library.model;

import java.util.Objects;

public class Professor extends User {
    private String employeeId;
    private String department;

    // Constructor with parameters
    public Professor(String name, String email, String employeeId, String department) {
        super(name, email, "Professor");
        this.employeeId = employeeId;
        this.department = department;
    }

    // Getters and setters
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // Override equals and hashCode for better comparison and hashing
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Professor professor = (Professor) o;
        return Objects.equals(employeeId, professor.employeeId) &&
                Objects.equals(department, professor.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), employeeId, department);
    }

    // Override toString for better readability
    @Override
    public String toString() {
        return "Professor{" +
                "employeeId='" + employeeId + '\'' +
                ", department='" + department + '\'' +
                "} " + super.toString();
    }
}
