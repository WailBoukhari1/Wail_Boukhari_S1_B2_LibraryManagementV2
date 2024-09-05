package org.library.model;

import java.time.LocalDate;
import org.library.service.Borrowable;
import org.library.service.Reservable;

public class Book extends Document implements Borrowable, Reservable {
    private String isbn;
    private int pageCount;
    private boolean borrowed = false;
    private boolean reserved = false;

    public Book() {
        super();
        setType("Book");
    }

    public Book(String title, String author, LocalDate publicationDate, String isbn, int pageCount) {
        super(title, author, publicationDate, "Book");
        this.isbn = isbn;
        this.pageCount = pageCount;
    }

    // Getters and setters
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
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
    public boolean isReserved() {
        return reserved;
    }

    @Override
    public void reserve() {
        reserved = true;
    }

    @Override
    public void cancelReservation() {
        reserved = false;
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", pageCount=" + pageCount +
                "} " + super.toString();
    }
}
