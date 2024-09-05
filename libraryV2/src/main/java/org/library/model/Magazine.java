package org.library.model;

import java.time.LocalDate;
import org.library.service.Borrowable;

public class Magazine extends Document implements Borrowable {
    private String issn;
    private int issueNumber;
    private boolean borrowed = false;

    public Magazine() {
        super();
        setType("Magazine");
    }

    public Magazine(String title, String author, LocalDate publicationDate, String issn, int issueNumber) {
        super(title, author, publicationDate, "Magazine");
        this.issn = issn;
        this.issueNumber = issueNumber;
    }

    // Getters and setters
    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(int issueNumber) {
        this.issueNumber = issueNumber;
    }

    @Override
    public boolean isBorrowed() {
        return borrowed;
    }

    @Override
    public void borrow() {
        borrowed = true;
    }

    @Override
    public void returnItem() {
        borrowed = false;
    }

    @Override
    public String toString() {
        return "Magazine{" +
                "issn='" + issn + '\'' +
                ", issueNumber=" + issueNumber +
                "} " + super.toString();
    }
}
