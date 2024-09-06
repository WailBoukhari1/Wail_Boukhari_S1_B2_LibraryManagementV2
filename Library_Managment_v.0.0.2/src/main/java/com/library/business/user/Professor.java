package com.library.business.user;

public class Professor extends User {
    private String department;
    private String researchArea;

    public Professor(String id, String name, String email, String department, String researchArea) {
        super(id, name, email);
        this.department = department;
        this.researchArea = researchArea;
    }

    // Getters and setters
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getResearchArea() { return researchArea; }
    public void setResearchArea(String researchArea) { this.researchArea = researchArea; }

    @Override
    public int getMaxBorrowDays() {
        return 30; // Professors can borrow for 30 days
    }
}
