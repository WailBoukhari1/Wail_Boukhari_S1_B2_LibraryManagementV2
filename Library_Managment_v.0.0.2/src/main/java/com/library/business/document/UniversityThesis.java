package com.library.business.document;

import java.time.LocalDate;

public class UniversityThesis extends Document {
    private String university;
    private String degree;

    public UniversityThesis(String id, String title, String author, LocalDate publicationDate, String university, String degree) {
        super(id, title, author, publicationDate);
        this.university = university;
        this.degree = degree;
    }

    // Getters and setters
    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }
    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }
}
