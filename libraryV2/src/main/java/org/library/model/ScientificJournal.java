package org.library.model;

import org.library.service.Borrowable;
import org.library.service.Reservable;

import java.time.LocalDate;
import java.util.Objects;

public class ScientificJournal extends Document implements Borrowable, Reservable {
    private String issn;
    private String researchDomain;
    private boolean borrowed = false;
    private boolean reserved = false;

    // Constructor with parameters
    public ScientificJournal(String title, String author, LocalDate publicationDate, String issn, String researchDomain) {
        super(title, author, publicationDate, "ScientificJournal");
        this.issn = issn;
        this.researchDomain = researchDomain;
    }

    // Getters and setters
    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getResearchDomain() {
        return researchDomain;
    }

    public void setResearchDomain(String researchDomain) {
        this.researchDomain = researchDomain;
    }

    @Override
    public boolean isBorrowed() {
        return borrowed;
    }

    @Override
    public void borrow() {
        borrowed = true;
        System.out.println("Scientific Journal has been borrowed.");
    }

    @Override
    public void returnItem() {
        borrowed = false;
        System.out.println("Scientific Journal has been returned.");
    }

    @Override
    public boolean isReserved() {
        return reserved;
    }

    @Override
    public void reserve() {
        reserved = true;
        System.out.println("Scientific Journal has been reserved.");
    }

    @Override
    public void cancelReservation() {
        reserved = false;
        System.out.println("Reservation for Scientific Journal has been canceled.");
    }

    // Override equals and hashCode for better comparison and hashing
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ScientificJournal that = (ScientificJournal) o;
        return borrowed == that.borrowed &&
                reserved == that.reserved &&
                Objects.equals(issn, that.issn) &&
                Objects.equals(researchDomain, that.researchDomain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), issn, researchDomain, borrowed, reserved);
    }

    // Override toString for better readability
    @Override
    public String toString() {
        return "ScientificJournal{" +
                "issn='" + issn + '\'' +
                ", researchDomain='" + researchDomain + '\'' +
                ", borrowed=" + borrowed +
                ", reserved=" + reserved +
                "} " + super.toString();
    }
}
