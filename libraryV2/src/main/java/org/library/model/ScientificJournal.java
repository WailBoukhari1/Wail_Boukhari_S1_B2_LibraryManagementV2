package org.library.model;

import java.time.LocalDate;
import org.library.service.Borrowable;
import org.library.service.Reservable;

public class ScientificJournal extends Document implements Borrowable, Reservable {
    private String issn;
    private String researchDomain;
    private boolean isBorrowed;
    private boolean isReserved;

    public ScientificJournal() {
        super();
        setType("ScientificJournal");
    }

    public ScientificJournal(String title, String author, LocalDate publicationDate, String issn, String researchDomain) {
        super(title, author, publicationDate, "ScientificJournal");
        this.issn = issn;
        this.researchDomain = researchDomain;
        this.isBorrowed = false;
        this.isReserved = false;
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

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public boolean isReserved() {
        return isReserved;
    }

    @Override
    public void borrow() {
        if (!isBorrowed) {
            isBorrowed = true;
            System.out.println("Scientific Journal has been borrowed.");
        } else {
            System.out.println("Scientific Journal is already borrowed.");
        }
    }

    @Override
    public void returnItem() {
        if (isBorrowed) {
            isBorrowed = false;
            System.out.println("Scientific Journal has been returned.");
        } else {
            System.out.println("Scientific Journal was not borrowed.");
        }
    }

    @Override
    public void reserve() {
        if (!isReserved) {
            isReserved = true;
            System.out.println("Scientific Journal has been reserved.");
        } else {
            System.out.println("Scientific Journal is already reserved.");
        }
    }

    @Override
    public void cancelReservation() {
        if (isReserved) {
            isReserved = false;
            System.out.println("Reservation for Scientific Journal has been canceled.");
        } else {
            System.out.println("Scientific Journal was not reserved.");
        }
    }

    @Override
    public String toString() {
        return "ScientificJournal{" +
                "issn='" + issn + '\'' +
                ", researchDomain='" + researchDomain + '\'' +
                ", isBorrowed=" + isBorrowed +
                ", isReserved=" + isReserved +
                "} " + super.toString();
    }
}
