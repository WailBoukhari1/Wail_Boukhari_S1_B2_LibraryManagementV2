package org.library.model;

public class Professor extends User {
    private String employeeId;
    private String department;

    public Professor() {
        super();
        setUserType("Professor");
    }

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

    @Override
    public String toString() {
        return "Professor{" +
                "employeeId='" + employeeId + '\'' +
                ", department='" + department + '\'' +
                "} " + super.toString();
    }
}
