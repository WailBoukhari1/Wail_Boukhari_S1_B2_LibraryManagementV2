package org.library.model;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Document {
    private Long id;
    private String title;
    private String author;
    private LocalDate publicationDate;
    private String type;

    // Constructor with parameters
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

    // Override equals and hashCode for better comparison and hashing
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(id, document.id) &&
                Objects.equals(title, document.title) &&
                Objects.equals(author, document.author) &&
                Objects.equals(publicationDate, document.publicationDate) &&
                Objects.equals(type, document.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, publicationDate, type);
    }

    // Override toString for better readability
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

    // Factory method pattern implementation
    public static Document createDocument(String title, String author, LocalDate publicationDate, String type) {
        return new Document(title, author, publicationDate, type) {};
    }
}
