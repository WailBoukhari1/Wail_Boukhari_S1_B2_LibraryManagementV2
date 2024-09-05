package org.library.model;

import java.time.LocalDate;

public class Document {
    private Long id;
    private String title;
    private String author;
    private LocalDate publicationDate;
    private String type;

    // Constructors
    public Document() {}

    public Document(String title, String author, LocalDate publicationDate, String type) {
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.type = type;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publicationDate=" + publicationDate +
                ", type='" + type + '\'' +
                '}';
    }
}
