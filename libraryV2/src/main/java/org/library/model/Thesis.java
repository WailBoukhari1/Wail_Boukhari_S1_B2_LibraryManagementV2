package org.library.model;

import org.library.service.Borrowable;

import java.time.LocalDate;
import java.util.Objects;

public class Thesis extends Document implements Borrowable {
    private String university;
    private String domain;
    private boolean borrowed = false;

    // Constructor with parameters
    public Thesis(String title, String author, LocalDate publicationDate, String university, String domain) {
        super(title, author, publicationDate, "Thesis");
        this.university = university;
        this.domain = domain;
    }

    // Getters and setters
    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public boolean isBorrowed() {
        return borrowed;
    }

    @Override
    public void borrow() {
        if (!borrowed) {
            borrowed = true;
            System.out.println("Thesis has been borrowed.");
        } else {
            System.out.println("Thesis is already borrowed.");
        }
    }

    @Override
    public void returnItem() {
        if (borrowed) {
            borrowed = false;
            System.out.println("Thesis has been returned.");
        } else {
            System.out.println("Thesis was not borrowed.");
        }
    }

    // Override equals and hashCode for better comparison and hashing
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Thesis thesis = (Thesis) o;
        return borrowed == thesis.borrowed &&
                Objects.equals(university, thesis.university) &&
                Objects.equals(domain, thesis.domain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), university, domain, borrowed);
    }

    // Override toString for better readability
    @Override
    public String toString() {
        return "Thesis{" +
                "university='" + university + '\'' +
                ", domain='" + domain + '\'' +
                ", borrowed=" + borrowed +
                "} " + super.toString();
    }
}
