package com.library.business.document;

import com.library.business.Borrowable;
import com.library.business.Reservable;

import java.time.LocalDate;

public class Book extends Document implements Borrowable, Reservable {
    private String isbn;
    private int pages;
    private LocalDate dueDate;
    private String reservedBy;

    public Book(String id, String title, String author, LocalDate publicationDate, String isbn, int pages) {
        super(id, title, author, publicationDate);
        this.isbn = isbn;
        this.pages = pages;
    }

    // Getters and setters
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public int getPages() { return pages; }
    public void setPages(int pages) { this.pages = pages; }

    // Borrowable interface methods
    @Override
    public void borrow(String userId) {
        setAvailable(false);
        // Logic to set due date based on user type
    }

    @Override
    public void returnItem() {
        setAvailable(true);
        setDueDate(null);
    }

    @Override
    public LocalDate getDueDate() { return dueDate; }

    @Override
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    // Reservable interface methods
    @Override
    public void reserve(String userId) {
        this.reservedBy = userId;
    }

    @Override
    public void cancelReservation() {
        this.reservedBy = null;
    }

    @Override
    public boolean isReserved() {
        return reservedBy != null;
    }

    @Override
    public String getReservedBy() {
        return reservedBy;
    }
}
