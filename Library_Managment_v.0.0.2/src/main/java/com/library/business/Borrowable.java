package com.library.business;

import java.time.LocalDate;

public interface Borrowable {
    void borrow(String userId);
    void returnItem();
    LocalDate getDueDate();
    void setDueDate(LocalDate dueDate);
}
