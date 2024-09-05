package org.library;

import org.library.service.Library;
import org.library.ui.ConsoleUI;
import org.library.dao.UserDAO;
import org.library.dao.DocumentDAO;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize DAOs
            UserDAO userDAO = new UserDAO();
            DocumentDAO documentDAO = new DocumentDAO();

            // Initialize Library service
            Library library = new Library(userDAO, documentDAO);

            // Initialize ConsoleUI
            ConsoleUI ui = new ConsoleUI(library);

            // Start the application
            ui.start();
        } catch (RuntimeException e) {
            System.err.println("Failed to start the application: " + e.getMessage());
            Throwable cause = e.getCause();
            if (cause != null) {
                System.err.println("Caused by: " + cause.getMessage());
            }
            e.printStackTrace();
        }
    }
}
