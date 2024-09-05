package org.library.model;

import java.time.LocalDate;
import org.library.service.Borrowable;

public class Thesis extends Document implements Borrowable {
    private String university;
    private String domain;
    private boolean isBorrowed;

    public Thesis() {
        super();
        setType("Thesis");
    }

    public Thesis(String title, String author, LocalDate publicationDate, String university, String domain) {
        super(title, author, publicationDate, "Thesis");
        this.university = university;
        this.domain = domain;
        this.isBorrowed = false;
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

    public boolean isBorrowed() {
        return isBorrowed;
    }

    @Override
    public void borrow() {
        if (!isBorrowed) {
            isBorrowed = true;
            System.out.println("Thesis has been borrowed.");
        } else {
            System.out.println("Thesis is already borrowed.");
        }
    }

    @Override
    public void returnItem() {
        if (isBorrowed) {
            isBorrowed = false;
            System.out.println("Thesis has been returned.");
        } else {
            System.out.println("Thesis was not borrowed.");
        }
    }

    @Override
    public String toString() {
        return "Thesis{" +
                "university='" + university + '\'' +
                ", domain='" + domain + '\'' +
                ", isBorrowed=" + isBorrowed +
                "} " + super.toString();
    }
}
