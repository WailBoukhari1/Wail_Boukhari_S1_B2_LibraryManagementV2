package com.library.business.document;

import java.time.LocalDate;

public class ScientificJournal extends Document {
    private String issn;
    private String researchField;

    public ScientificJournal(String id, String title, String author, LocalDate publicationDate, String issn, String researchField) {
        super(id, title, author, publicationDate);
        this.issn = issn;
        this.researchField = researchField;
    }

    // Getters and setters
    public String getIssn() { return issn; }
    public void setIssn(String issn) { this.issn = issn; }
    public String getResearchField() { return researchField; }
    public void setResearchField(String researchField) { this.researchField = researchField; }
}
