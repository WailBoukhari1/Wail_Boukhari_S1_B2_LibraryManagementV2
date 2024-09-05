package org.library.model;

import org.library.service.Borrowable;

import java.time.LocalDate;
import java.util.Objects;

public class Magazine extends Document implements Borrowable {
    private String issn;
    private int issueNumber;
    private boolean borrowed = false;

    // Constructor with parameters
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

    // Override equals and hashCode for better comparison and hashing
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Magazine magazine = (Magazine) o;
        return issueNumber == magazine.issueNumber &&
                borrowed == magazine.borrowed &&
                Objects.equals(issn, magazine.issn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), issn, issueNumber, borrowed);
    }

    // Override toString for better readability
    @Override
    public String toString() {
        return "Magazine{" +
                "issn='" + issn + '\'' +
                ", issueNumber=" + issueNumber +
                ", borrowed=" + borrowed +
                "} " + super.toString();
    }
}
