package com.library;

import com.library.app.ConsoleUI;
import com.library.service.LibraryService;
import com.library.service.LoanService;
import com.library.service.ReservationService;
import com.library.service.UserService;

public class Main {
    public static void main(String[] args) {
        // Initialize services
        LibraryService libraryService = new LibraryService();
        UserService userService = new UserService();
        ReservationService reservationService = new ReservationService();
        LoanService loanService = new LoanService(reservationService);
        reservationService.setLoanService(loanService);

        // Initialize and start the console UI
        ConsoleUI consoleUI = new ConsoleUI(libraryService, loanService, reservationService, userService);
        consoleUI.start();
    }
}