package com.library.app;

import com.library.service.DocumentService;
import com.library.service.LoanService;
import com.library.service.ReservationService;
import com.library.service.UserService;
import com.library.model.document.*;
import com.library.model.user.*;
import com.library.model.Loan;
import com.library.model.Reservation;
import com.library.util.InputValidator;
import com.library.util.DateUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleUI {
    private final DocumentService documentService;
    private final LoanService loanService;
    private final ReservationService reservationService;
    private final UserService userService;
    private final Scanner scanner;

    public ConsoleUI(DocumentService documentService, LoanService loanService,
                     ReservationService reservationService, UserService userService) {
        this.documentService = documentService;
        this.loanService = loanService;
        this.reservationService = reservationService;
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            printMainMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    userMenu();
                    break;
                case "2":
                    documentMenu();
                    break;
                case "3":
                    reservationMenu();
                    break;
                case "4":
                    loanMenu();
                    break;
                case "5":
                    searchDocuments();
                    break;
                case "6":
                    listAllDocuments();
                    break;
                case "0":
                    System.out.println("👋 Exiting...");
                    return;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n📚 Library Management System 📚");
        System.out.println("1. 👤 User Management");
        System.out.println("2. 📖 Document Management");
        System.out.println("3. 🔖 Reservation Management");
        System.out.println("4. 📤 Loan Management");
        System.out.println("5. 🔍 Search Documents");
        System.out.println("6. 📚 List All Documents");
        System.out.println("0. 🚪 Exit");
        System.out.print("Enter your choice: ");
    }

    private void userMenu() {
        while (true) {
            System.out.println("\n👤 User Management");
            System.out.println("1. Create User");
            System.out.println("2. Update User");
            System.out.println("3. Delete User");
            System.out.println("4. List All Users");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    createUser();
                    break;
                case "2":
                    updateUser();
                    break;
                case "3":
                    deleteUser();
                    break;
                case "4":
                    listAllUsers();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    private void documentMenu() {
        while (true) {
            System.out.println("\n📖 Document Management");
            System.out.println("1. Create Document");
            System.out.println("2. Update Document");
            System.out.println("3. Delete Document");
            System.out.println("4. List All Documents");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    createDocument();
                    break;
                case "2":
                    updateDocument();
                    break;
                case "3":
                    deleteDocument();
                    break;
                case "4":
                    listAllDocuments();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    private void reservationMenu() {
        while (true) {
            System.out.println("\n🔖 Reservation Management");
            System.out.println("1. Reserve Document");
            System.out.println("2. Cancel Reservation");
            System.out.println("3. List All Reservations");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    reserveDocument();
                    break;
                case "2":
                    cancelReservation();
                    break;
                case "3":
                    listAllReservations();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    private void loanMenu() {
        while (true) {
            System.out.println("\n📤 Loan Management");
            System.out.println("1. Loan Document");
            System.out.println("2. Return Document");
            System.out.println("3. List All Loans");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    loanDocument();
                    break;
                case "2":
                    returnDocument();
                    break;
                case "3":
                    listAllLoans();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    // User management methods
    private void createUser() {
        System.out.print("Enter user name: ");
        String name = scanner.nextLine();
        if (!InputValidator.isValidName(name)) {
            System.out.println("❌ Invalid name. Please try again.");
            return;
        }
        System.out.print("Enter user email: ");
        String email = scanner.nextLine();
        System.out.print("Enter user phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter user type (Student/Professor): ");
        String type = scanner.nextLine();

        User user;
        if (type.equalsIgnoreCase("Student")) {
            System.out.print("Enter student ID: ");
            String studentId = scanner.nextLine();
            System.out.print("Enter department: ");
            String department = scanner.nextLine();
            user = new Student(UUID.randomUUID(), name, email, phoneNumber, studentId, department);
        } else if (type.equalsIgnoreCase("Professor")) {
            System.out.print("Enter department: ");
            String department = scanner.nextLine();
            user = new Professor(UUID.randomUUID(), name, email, phoneNumber, department);
        } else {
            System.out.println("❌ Invalid user type. Please try again.");
            return;
        }

        userService.addUser(user);
        System.out.println("✅ User created successfully.");
    }

    private void updateUser() {
        System.out.print("Enter user name to update: ");
        String userName = scanner.nextLine();

        Optional<User> optionalUser = userService.getUserByName(userName);
        if (optionalUser.isEmpty()) {
            System.out.println("❌ User not found. Please try again.");
            return;
        }

        User user = optionalUser.get();

        System.out.print("Enter new email (or press enter to keep current): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            user.setEmail(email);
        }

        System.out.print("Enter new phone number (or press enter to keep current): ");
        String phoneNumber = scanner.nextLine();
        if (!phoneNumber.isEmpty()) {
            user.setPhoneNumber(phoneNumber);
        }

        userService.updateUser(user);
        System.out.println("✅ User updated successfully.");
    }

    private void deleteUser() {
        System.out.print("Enter user name to delete: ");
        String userName = scanner.nextLine();
    
        userService.deleteUser(userName);
        System.out.println("✅ User deleted successfully.");
    }

    private void listAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("❌ No users found.");
        } else {
            System.out.println("👥 All users:");
            for (User user : users) {
                System.out.println("-----------------------------------");
                System.out.println("👤 Name: " + user.getName());
                System.out.println("📧 Email: " + user.getEmail());
                System.out.println("📞 Phone: " + user.getPhoneNumber());
                if (user instanceof Student) {
                    System.out.println("🎓 Student ID: " + ((Student) user).getStudentId());
                    System.out.println("🏫 Department: " + ((Student) user).getDepartment());
                } else if (user instanceof Professor) {
                    System.out.println("🏫 Department: " + ((Professor) user).getDepartment());
                }
            }
            System.out.println("-----------------------------------");
        }
    }

    // Document management methods
    private void createDocument() {
        System.out.print("Enter document title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter publication year: ");
        int publicationYear = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter document type (Book/Magazine/ScientificJournal/UniversityThesis): ");
        String type = scanner.nextLine();

        Document document;
        UUID id = UUID.randomUUID();
        switch (type.toLowerCase()) {
            case "book":
                document = new Book(id, title, author, publisher, publicationYear);
                break;
            case "magazine":
                document = new Magazine(id, title, author, publisher, publicationYear);
                break;
            case "scientificjournal":
                System.out.print("Enter research field: ");
                String researchField = scanner.nextLine();
                document = new ScientificJournal(id, title, author, publisher, publicationYear, researchField);
                break;
            case "universitythesis":
                System.out.print("Enter university: ");
                String university = scanner.nextLine();
                System.out.print("Enter field: ");
                String field = scanner.nextLine();
                document = new UniversityThesis(id, title, author, publisher, publicationYear, university, field);
                break;
            default:
                System.out.println("❌ Invalid document type. Please try again.");
                return;
        }

        documentService.addDocument(document);
        System.out.println("✅ Document created successfully.");
    }

    private void updateDocument() {
        System.out.print("Enter document title to update: ");
        String documentTitle = scanner.nextLine();

        Optional<Document> optionalDocument = documentService.getDocumentByTitle(documentTitle);
        if (optionalDocument.isEmpty()) {
            System.out.println("❌ Document not found. Please try again.");
            return;
        }

        Document document = optionalDocument.get();

        System.out.print("Enter new author (or press enter to keep current): ");
        String author = scanner.nextLine();
        if (!author.isEmpty()) {
            document.setAuthor(author);
        }

        System.out.print("Enter new publisher (or press enter to keep current): ");
        String publisher = scanner.nextLine();
        if (!publisher.isEmpty()) {
            document.setPublisher(publisher);
        }

        System.out.print("Enter new publication year (or press enter to keep current): ");
        String publicationYearStr = scanner.nextLine();
        if (!publicationYearStr.isEmpty()) {
            int publicationYear = Integer.parseInt(publicationYearStr);
            document.setPublicationYear(publicationYear);
        }

        documentService.updateDocument(document);
        System.out.println("✅ Document updated successfully.");
    }

    private void deleteDocument() {
        System.out.print("Enter document title to delete: ");
        String documentTitle = scanner.nextLine();

        documentService.deleteDocument(documentTitle);
        System.out.println("✅ Document deleted successfully.");
    }

    private void listAllDocuments() {
        List<Document> documents = documentService.getAllDocuments();
        if (documents.isEmpty()) {
            System.out.println("❌ No documents found.");
        } else {
            System.out.println("📚 All documents:");
            for (Document doc : documents) {
                System.out.println("-----------------------------------");
                System.out.println("📕 Title: " + doc.getTitle());
                System.out.println("✍️ Author: " + doc.getAuthor());
                System.out.println("🏢 Publisher: " + doc.getPublisher());
                System.out.println("📅 Publication Year: " + doc.getPublicationYear());
                System.out.println("📊 Type: " + doc.getType());
                if (doc instanceof ScientificJournal) {
                    System.out.println("🔬 Research Field: " + ((ScientificJournal) doc).getResearchField());
                } else if (doc instanceof UniversityThesis) {
                    System.out.println("🎓 University: " + ((UniversityThesis) doc).getUniversity());
                    System.out.println("📚 Field: " + ((UniversityThesis) doc).getField());
                }
                boolean isLoaned = loanService.isDocumentLoaned(doc.getTitle());
                System.out.println("📊 Status: " + (isLoaned ? "🔴 Loaned" : "🟢 Available"));

                List<Reservation> reservations = reservationService.getReservationsForDocument(doc.getTitle());
                if (!reservations.isEmpty()) {
                    System.out.println("🔖 Reserved by:");
                    for (Reservation reservation : reservations) {
                        System.out.println("   👤 " + reservation.getUserName());
                    }
                }
            }
            System.out.println("-----------------------------------");
        }
    }

    // Reservation management methods
    private void reserveDocument() {
        System.out.print("Enter user name: ");
        String userName = scanner.nextLine();
        System.out.print("Enter document title: ");
        String documentTitle = scanner.nextLine();

        try {
            reservationService.reserveDocument(documentTitle, userName);
            System.out.println("✅ Document reserved successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void cancelReservation() {
        System.out.print("Enter user name: ");
        String userName = scanner.nextLine();
        System.out.print("Enter document title: ");
        String documentTitle = scanner.nextLine();

        try {
            reservationService.cancelReservation(documentTitle, userName);
            System.out.println("✅ Reservation canceled successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void listAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("❌ No reservations found.");
        } else {
            System.out.println("🔖 All reservations:");
            for (Reservation reservation : reservations) {
                System.out.println("-----------------------------------");
                System.out.println("📕 Document: " + reservation.getDocumentTitle());
                System.out.println("👤 User: " + reservation.getUserName());
                System.out.println("📅 Reservation Date: " + reservation.getReservationDate());
            }
            System.out.println("-----------------------------------");
        }
    }

    // Loan management methods
    private void loanDocument() {
        System.out.print("Enter user name: ");
        String userName = scanner.nextLine();
        System.out.print("Enter document title: ");
        String documentTitle = scanner.nextLine();
        System.out.print("Enter loan date (dd-MM-yyyy): ");
        String loanDateStr = scanner.nextLine();
        if (!DateUtils.isValidDate(loanDateStr)) {
            System.out.println("❌ Invalid date format. Please try again.");
            return;
        }
        LocalDate loanDate = DateUtils.parseDate(loanDateStr);

        try {
            loanService.loanDocument(documentTitle, userName);
            System.out.println("✅ Document loaned successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private void returnDocument() {
        System.out.print("Enter user name: ");
        String userName = scanner.nextLine();
        System.out.print("Enter document title: ");
        String documentTitle = scanner.nextLine();
        System.out.print("Enter return date (dd-MM-yyyy): ");
        String returnDateStr = scanner.nextLine();
        if (!DateUtils.isValidDate(returnDateStr)) {
            System.out.println("❌ Invalid date format. Please try again.");
            return;
        }
        LocalDate returnDate = DateUtils.parseDate(returnDateStr);

        try {
            loanService.returnDocument(documentTitle, userName);
            System.out.println("✅ Document returned successfully.");

            // Check if there are any reservations for this document
            List<Reservation> reservations = reservationService.getReservationsForDocument(documentTitle);
            if (!reservations.isEmpty()) {
                System.out.println("📢 Notification: The document '" + documentTitle +
                        "' is now available for user '" + reservations.get(0).getUserName() + "'");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private void listAllLoans() {
        List<Loan> loans = loanService.getAllLoans();
        if (loans.isEmpty()) {
            System.out.println("❌ No loans found.");
        } else {
            System.out.println("📚 All loans:");
            for (Loan loan : loans) {
                System.out.println("-----------------------------------");
                System.out.println("📕 Document: " + loan.getDocumentTitle());
                System.out.println("👤 User: " + loan.getUserName());
                System.out.println("📅 Loan Date: " + DateUtils.formatDate(loan.getLoanDate()));
                System.out.println("📅 Return Date: " +
                        (loan.getReturnDate() != null ? DateUtils.formatDate(loan.getReturnDate()) : "Not returned yet"));

                // Calculate and display the loan duration or days overdue
                if (loan.getReturnDate() != null) {
                    long daysLoaned = ChronoUnit.DAYS.between(loan.getLoanDate(), loan.getReturnDate());
                    System.out.println("⏳ Loan Duration: " + daysLoaned + " days");
                } else {
                    long daysOverdue = ChronoUnit.DAYS.between(loan.getLoanDate(), LocalDate.now()) - 14; // Assuming 14-day loan period
                    if (daysOverdue > 0) {
                        System.out.println("⚠️ Overdue by: " + daysOverdue + " days");
                    } else {
                        System.out.println("✅ Due in: " + (-daysOverdue) + " days");
                    }
                }
            }
            System.out.println("-----------------------------------");
        }
    }

    private void searchDocuments() {
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine();
        List<Document> documents = documentService.searchDocuments(searchTerm);
        if (documents.isEmpty()) {
            System.out.println("❌ No documents found.");
        } else {
            System.out.println("📚 Found documents:");
            for (Document doc : documents) {
                System.out.println("📖 " + doc.getTitle() + " - ✍️ " + doc.getAuthor() + " (🏷️ " + doc.getType() + ")");
            }
        }
    }
}        