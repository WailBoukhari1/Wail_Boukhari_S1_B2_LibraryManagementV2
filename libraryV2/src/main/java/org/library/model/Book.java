package org.library.model;

import org.library.service.Borrowable;
import org.library.service.Reservable;

import java.time.LocalDate;
import java.util.Objects;

public class Book extends Document implements Borrowable, Reservable {
    private String isbn;
    private int pageCount;
    private boolean borrowed = false;
    private boolean reserved = false;

    // Constructor with parameters
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

    // Override equals and hashCode for better comparison and hashing
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Book book = (Book) o;
        return pageCount == book.pageCount &&
                borrowed == book.borrowed &&
                reserved == book.reserved &&
                Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isbn, pageCount, borrowed, reserved);
    }

    // Override toString for better readability
    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", pageCount=" + pageCount +
                ", borrowed=" + borrowed +
                ", reserved=" + reserved +
                "} " + super.toString();
    }
}
