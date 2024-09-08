package com.library.model.document;

import java.util.UUID;

public class Book extends Document {
    public Book(UUID id, String title, String author, String publisher, int publicationYear) {
        super(id, title, author, publisher, publicationYear);
    }

    @Override
    public String getType() {
        return "Book";
    }
}