package com.library.app;

import java.util.List;
import java.util.Scanner;

import com.library.model.Loan;
import com.library.model.Reservation;
import com.library.model.document.Book;
import com.library.model.document.Document;
import com.library.model.document.Magazine;
import com.library.model.document.ScientificJournal;
import com.library.model.document.UniversityThesis;
import com.library.model.user.Professor;
import com.library.model.user.Student;
import com.library.model.user.User;
import com.library.service.DocumentService;
import com.library.service.LoanService;
import com.library.service.ReservationService;
import com.library.service.UserService;

public class ConsoleUI {
    private final DocumentService documentService;
    private final LoanService loanService;
    private final ReservationService reservationService;
    private final UserService userService;
    private final Scanner scanner;

    public ConsoleUI(DocumentService documentService, LoanService loanService, ReservationService reservationService, UserService userService) {
        this.documentService = documentService;
        this.loanService = loanService;
        this.reservationService = reservationService;
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean exit = false;
        while (!exit) {
            printMainMenu();
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1 -> handleDocuments();
                case 2 -> handleUsers();
                case 3 -> handleLoans();
                case 4 -> handleReservations();
                case 5 -> exit = true;
                default -> System.out.println("❌ Invalid choice. Please try again.");
            }
        }
        System.out.println("👋 Thank you for using the Library Management System. Goodbye!");
    }

    private void printMainMenu() {
        System.out.println("\n📚 Library Management System");
        System.out.println("1. 📖 Manage Documents");
        System.out.println("2. 👥 Manage Users");
        System.out.println("3. 📅 Manage Loans");
        System.out.println("4. 🔖 Manage Reservations");
        System.out.println("5. 🚪 Exit");
    }

    private void handleDocuments() {
        boolean back = false;
        while (!back) {
            printDocumentMenu();
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1 -> addDocument();
                case 2 -> updateDocument();
                case 3 -> deleteDocument();
                case 4 -> searchDocuments();
                case 5 -> listAllDocuments();
                case 6 -> back = true;
                default -> System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    private void printDocumentMenu() {
        System.out.println("\n📖 Document Management");
        System.out.println("1. ➕ Add Document");
        System.out.println("2. 🔄 Update Document");
        System.out.println("3. ❌ Delete Document");
        System.out.println("4. 🔍 Search Documents");
        System.out.println("5. 📋 List All Documents");
        System.out.println("6. ⬅️ Back to Main Menu");
    }

    private void addDocument() {
        System.out.println("\n➕ Add New Document");
        String title = getStringInput("Enter title: ");
        String author = getStringInput("Enter author: ");
        String publisher = getStringInput("Enter publisher: ");
        int publicationYear = getIntInput("Enter publication year: ");
    
        System.out.println("Select document type:");
        System.out.println("1. 📘 Book");
        System.out.println("2. 📰 Magazine");
        System.out.println("3. 🔬 Scientific Journal");
        System.out.println("4. 🎓 University Thesis");
    
        int typeChoice = getIntInput("Enter your choice: ");
        Document document;
    
        switch (typeChoice) {
            case 1 -> {
                String isbn = getStringInput("Enter ISBN: ");
                document = new Book(null, title, author, publisher, publicationYear, isbn);
            }
            case 2 -> {
                int issueNumber = getIntInput("Enter issue number: ");
                document = new Magazine(null, title, author, publisher, publicationYear, issueNumber);
            }
            case 3 -> {
                String researchField = getStringInput("Enter research field: ");
                document = new ScientificJournal(null, title, author, publisher, publicationYear, researchField);
            }
            case 4 -> {
                String university = getStringInput("Enter university: ");
                String field = getStringInput("Enter field of study: ");
                document = new UniversityThesis(null, title, author, publisher, publicationYear, university, field);
            }
            default -> {
                System.out.println("❌ Invalid document type. Document not added.");
                return;
            }
        }
    
        documentService.addDocument(document);
        System.out.println("✅ Document added successfully!");
    }

    private void updateDocument() {
        System.out.println("\n🔄 Update Document");
        String title = getStringInput("Enter the title of the document to update: ");
        Document document = documentService.getDocumentByTitle(title).orElse(null);

        if (document == null) {
            System.out.println("❌ Document not found.");
            return;
        }

        System.out.println("Current document details:");
        printDocumentDetails(document);

        String newAuthor = getStringInput("Enter new author (press Enter to keep current): ");
        String newPublisher = getStringInput("Enter new publisher (press Enter to keep current): ");
        String newYearStr = getStringInput("Enter new publication year (press Enter to keep current): ");

        if (!newAuthor.isEmpty()) document.setAuthor(newAuthor);
        if (!newPublisher.isEmpty()) document.setPublisher(newPublisher);
        if (!newYearStr.isEmpty()) {
            try {
                int newYear = Integer.parseInt(newYearStr);
                document.setPublicationYear(newYear);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid year format. Year not updated.");
            }
        }

        documentService.updateDocument(document);
        System.out.println("✅ Document updated successfully!");
    }

    private void deleteDocument() {
        System.out.println("\n❌ Delete Document");
        String title = getStringInput("Enter the title of the document to delete: ");
        Document document = documentService.getDocumentByTitle(title).orElse(null);

        if (document == null) {
            System.out.println("❌ Document not found.");
            return;
        }

        System.out.println("Are you sure you want to delete this document?");
        printDocumentDetails(document);
        String confirm = getStringInput("Type 'YES' to confirm deletion: ");

        if (confirm.equalsIgnoreCase("YES")) {
            documentService.deleteDocument(title);
            System.out.println("✅ Document deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void searchDocuments() {
        System.out.println("\n🔍 Search Documents");
        String searchTerm = getStringInput("Enter search term: ");
        List<Document> results = documentService.searchDocuments(searchTerm);

        if (results.isEmpty()) {
            System.out.println("No documents found matching the search term.");
        } else {
            System.out.println("Search results:");
            for (Document doc : results) {
                printDocumentDetails(doc);
                System.out.println("--------------------");
            }
        }
    }

    private void listAllDocuments() {
        System.out.println("\n📋 All Documents");
        List<Document> documents = documentService.getAllDocuments();

        if (documents.isEmpty()) {
            System.out.println("No documents found in the library.");
        } else {
            for (Document doc : documents) {
                printDocumentDetails(doc);
                System.out.println("--------------------");
            }
        }
    }

    private void printDocumentDetails(Document doc) {
        System.out.println("Title: " + doc.getTitle());
        System.out.println("Author: " + doc.getAuthor());
        System.out.println("Publisher: " + doc.getPublisher());
        System.out.println("Publication Year: " + doc.getPublicationYear());
        System.out.println("Type: " + doc.getType());

        if (doc instanceof Book) {
            System.out.println("ISBN: " + ((Book) doc).getIsbn());
        } else if (doc instanceof Magazine) {
            System.out.println("Issue Number: " + ((Magazine) doc).getIssueNumber());
        } else if (doc instanceof ScientificJournal) {
            System.out.println("Research Field: " + ((ScientificJournal) doc).getResearchField());
        } else if (doc instanceof UniversityThesis) {
            UniversityThesis thesis = (UniversityThesis) doc;
            System.out.println("University: " + thesis.getUniversity());
            System.out.println("Field: " + thesis.getField());
        }
    }

    private void handleUsers() {
        boolean back = false;
        while (!back) {
            printUserMenu();
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1 -> addUser();
                case 2 -> updateUser();
                case 3 -> deleteUser();
                case 4 -> listAllUsers();
                case 5 -> back = true;
                default -> System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    private void printUserMenu() {
        System.out.println("\n👥 User Management");
        System.out.println("1. ➕ Add User");
        System.out.println("2. 🔄 Update User");
        System.out.println("3. ❌ Delete User");
        System.out.println("4. 📋 List All Users");
        System.out.println("5. ⬅️ Back to Main Menu");
    }

    private void addUser() {
        System.out.println("\n➕ Add New User");
        String name = getStringInput("Enter name: ");
        String email = getStringInput("Enter email: ");
        String phoneNumber = getStringInput("Enter phone number: ");

        System.out.println("Select user type:");
        System.out.println("1. 🎓 Student");
        System.out.println("2. 👨‍🏫 Professor");

        int typeChoice = getIntInput("Enter your choice: ");
        User user;

        switch (typeChoice) {
            case 1 -> {
                String studentId = getStringInput("Enter student ID: ");
                String department = getStringInput("Enter department: ");
                user = new Student(null, name, email, phoneNumber, studentId, department);
            }
            case 2 -> {
                String department = getStringInput("Enter department: ");
                user = new Professor(null, name, email, phoneNumber, department);
            }
            default -> {
                System.out.println("❌ Invalid user type. User not added.");
                return;
            }
        }

        userService.addUser(user);
        System.out.println("✅ User added successfully!");
    }

    private void updateUser() {
        System.out.println("\n🔄 Update User");
        String name = getStringInput("Enter the name of the user to update: ");
        User user = userService.getUserByName(name).orElse(null);

        if (user == null) {
            System.out.println("❌ User not found.");
            return;
        }

        System.out.println("Current user details:");
        printUserDetails(user);

        String newEmail = getStringInput("Enter new email (press Enter to keep current): ");
        String newPhoneNumber = getStringInput("Enter new phone number (press Enter to keep current): ");

        if (!newEmail.isEmpty()) user.setEmail(newEmail);
        if (!newPhoneNumber.isEmpty()) user.setPhoneNumber(newPhoneNumber);

        if (user instanceof Student) {
            String newStudentId = getStringInput("Enter new student ID (press Enter to keep current): ");
            String newDepartment = getStringInput("Enter new department (press Enter to keep current): ");
            if (!newStudentId.isEmpty()) ((Student) user).setStudentId(newStudentId);
            if (!newDepartment.isEmpty()) ((Student) user).setDepartment(newDepartment);
        } else if (user instanceof Professor) {
            String newDepartment = getStringInput("Enter new department (press Enter to keep current): ");
            if (!newDepartment.isEmpty()) ((Professor) user).setDepartment(newDepartment);
        }

        userService.updateUser(user);
        System.out.println("✅ User updated successfully!");
    }

    private void deleteUser() {
        System.out.println("\n❌ Delete User");
        String name = getStringInput("Enter the name of the user to delete: ");
        User user = userService.getUserByName(name).orElse(null);

        if (user == null) {
            System.out.println("❌ User not found.");
            return;
        }

        System.out.println("Are you sure you want to delete this user?");
        printUserDetails(user);
        String confirm = getStringInput("Type 'YES' to confirm deletion: ");

        if (confirm.equalsIgnoreCase("YES")) {
            userService.deleteUser(name);
            System.out.println("✅ User deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void listAllUsers() {
        System.out.println("\n📋 All Users");
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("No users found in the system.");
        } else {
            for (User user : users) {
                printUserDetails(user);
                System.out.println("--------------------");
            }
        }
    }

    private void printUserDetails(User user) {
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Phone Number: " + user.getPhoneNumber());
        System.out.println("Type: " + user.getType());

        if (user instanceof Student) {
            Student student = (Student) user;
            System.out.println("Student ID: " + student.getStudentId());
            System.out.println("Department: " + student.getDepartment());
        } else if (user instanceof Professor) {
            Professor professor = (Professor) user;
            System.out.println("Department: " + professor.getDepartment());
        }
    }

    private void handleLoans() {
        boolean back = false;
        while (!back) {
            printLoanMenu();
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1 -> loanDocument();
                case 2 -> returnDocument();
                case 3 -> listAllLoans();
                case 4 -> back = true;
                default -> System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    private void printLoanMenu() {
        System.out.println("\n📅 Loan Management");
        System.out.println("1. 📚 Loan Document");
        System.out.println("2. 🔙 Return Document");
        System.out.println("3. 📋 List All Loans");
        System.out.println("4. ⬅️ Back to Main Menu");
    }

    private void loanDocument() {
        System.out.println("\n📚 Loan Document");
        String documentTitle = getStringInput("Enter document title: ");
        String userName = getStringInput("Enter user name: ");

        try {
            loanService.loanDocument(documentTitle, userName);
            System.out.println("✅ Document loaned successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("❌ An error occurred: " + e.getMessage());
        }
    }

    private void returnDocument() {
        System.out.println("\n🔙 Return Document");
        String documentTitle = getStringInput("Enter document title: ");
        String userName = getStringInput("Enter user name: ");

        try {
            loanService.returnDocument(documentTitle, userName);
            System.out.println("✅ Document returned successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("❌ An error occurred: " + e.getMessage());
        }
    }

    private void listAllLoans() {
        System.out.println("\n📋 All Loans");
        List<Loan> loans = loanService.getAllLoans();

        if (loans.isEmpty()) {
            System.out.println("No active loans found.");
        } else {
            for (Loan loan : loans) {
                printLoanDetails(loan);
                System.out.println("--------------------");
            }
        }
    }

    private void printLoanDetails(Loan loan) {
        System.out.println("Document: " + loan.getDocumentTitle());
        System.out.println("User: " + loan.getUserName());
        System.out.println("Loan Date: " + loan.getLoanDate());
        System.out.println("Return Date: " + (loan.getReturnDate() != null ? loan.getReturnDate() : "Not returned yet"));
    }

    private void handleReservations() {
        boolean back = false;
        while (!back) {
            printReservationMenu();
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1 -> reserveDocument();
                case 2 -> cancelReservation();
                case 3 -> listAllReservations();
                case 4 -> back = true;
                default -> System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    private void printReservationMenu() {
        System.out.println("\n🔖 Reservation Management");
        System.out.println("1. 📌 Reserve Document");
        System.out.println("2. ❌ Cancel Reservation");
        System.out.println("3. 📋 List All Reservations");
        System.out.println("4. ⬅️ Back to Main Menu");
    }

    private void reserveDocument() {
        System.out.println("\n📌 Reserve Document");
        String documentTitle = getStringInput("Enter document title: ");
        String userName = getStringInput("Enter user name: ");

        try {
            reservationService.reserveDocument(documentTitle, userName);
            System.out.println("✅ Document reserved successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("❌ An error occurred: " + e.getMessage());
        }
    }

    private void cancelReservation() {
        System.out.println("\n❌ Cancel Reservation");
        String documentTitle = getStringInput("Enter document title: ");
        String userName = getStringInput("Enter user name: ");

        try {
            reservationService.cancelReservation(documentTitle, userName);
            System.out.println("✅ Reservation cancelled successfully!");
        } catch (RuntimeException e) {
            System.out.println("❌ An error occurred: " + e.getMessage());
        }
    }

    private void listAllReservations() {
        System.out.println("\n📋 All Reservations");
        List<Reservation> reservations = reservationService.getAllReservations();

        if (reservations.isEmpty()) {
            System.out.println("No active reservations found.");
        } else {
            for (Reservation reservation : reservations) {
                printReservationDetails(reservation);
                System.out.println("--------------------");
            }
        }
    }

    private void printReservationDetails(Reservation reservation) {
        System.out.println("Document: " + reservation.getDocumentTitle());
        System.out.println("User: " + reservation.getUserName());
        System.out.println("Reservation Date: " + reservation.getReservationDate());
    }

    private String getStringInput(String prompt) {
           System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a number.");
            }
        }
    }
}
