package com.library.business.user;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
    protected String id;
    protected String name;
    protected String email;
    protected List<String> borrowedDocuments;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.borrowedDocuments = new ArrayList<>();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<String> getBorrowedDocuments() { return borrowedDocuments; }

    public void borrowDocument(String documentId) {
        borrowedDocuments.add(documentId);
    }

    public void returnDocument(String documentId) {
        borrowedDocuments.remove(documentId);
    }

    public abstract int getMaxBorrowDays();
}
