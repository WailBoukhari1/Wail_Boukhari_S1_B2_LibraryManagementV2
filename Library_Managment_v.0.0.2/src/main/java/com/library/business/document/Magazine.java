package com.library.business.document;

import java.time.LocalDate;

public class Magazine extends Document {
    private String issn;
    private int issueNumber;

    public Magazine(String id, String title, String author, LocalDate publicationDate, String issn, int issueNumber) {
        super(id, title, author, publicationDate);
        this.issn = issn;
        this.issueNumber = issueNumber;
    }

    // Getters and setters
    public String getIssn() { return issn; }
    public void setIssn(String issn) { this.issn = issn; }
    public int getIssueNumber() { return issueNumber; }
    public void setIssueNumber(int issueNumber) { this.issueNumber = issueNumber; }
}
