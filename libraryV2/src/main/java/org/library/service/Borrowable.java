package org.library.service;

public interface Borrowable {
    void borrow();
    void returnItem();
    boolean isBorrowed();
}
