package com.library.model.document;

import java.util.UUID;

public class Magazine extends Document {
    public Magazine(UUID id, String title, String author, String publisher, int publicationYear) {
        super(id, title, author, publisher, publicationYear);
    }

    @Override
    public String getType() {
        return "Magazine";
    }
}