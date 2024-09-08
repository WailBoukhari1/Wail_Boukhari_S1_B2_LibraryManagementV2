package com.library.app;

import com.library.service.LibraryService;
import com.library.service.LoanService;
import com.library.service.ReservationService;
import com.library.service.UserService;
import com.library.util.InputValidator;
import com.library.util.DateUtils;
import com.library.model.document.Document;
import com.library.model.document.ScientificJournal;
import com.library.model.document.UniversityThesis;
import com.library.model.user.Professor;
import com.library.model.user.Student;
import com.library.model.user.User;
import com.library.model.Loan;
import com.library.model.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;

public class ConsoleUI {
    private final LibraryService libraryService;
    private final LoanService loanService;
    private final ReservationService reservationService;
    private final UserService userService;
    private final Scanner scanner;

    public ConsoleUI(LibraryService libraryService, LoanService loanService, ReservationService reservationService, UserService userService) {
        this.libraryService = libraryService;
        this.loanService = loanService;
        this.reservationService = reservationService;
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            printMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addUser();
                    break;
                case "2":
                    addDocument();
                    break;
                case "3":
                    reserveDocument();
                    break;
                case "4":
                    loanDocument();
                    break;
                case "5":
                    returnDocument();
                    break;
                case "6":
                    searchDocuments();
                    break;
                case "7":
                    listAllUsers();
                    break;
                case "8":
                    listAllDocuments();
                    break;
                case "9":
                    System.out.println("👋 Exiting...");
                    return;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n📚 Library Management System 📚");
        System.out.println("1. 👤 Add User");
        System.out.println("2. 📖 Add Document");
        System.out.println("3. 🔖 Reserve Document");
        System.out.println("4. 📤 Loan Document");
        System.out.println("5. 📥 Return Document");
        System.out.println("6. 🔍 Search Documents");
        System.out.println("7. 👥 List All Users");
        System.out.println("8. 📚 List All Documents");
        System.out.println("9. 🚪 Exit");
        System.out.print("Enter your choice: ");
    }

    private void addUser() {
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
            user = new com.library.model.user.Student(UUID.randomUUID(), name, email, phoneNumber, studentId);
        } else if (type.equalsIgnoreCase("Professor")) {
            System.out.print("Enter department: ");
            String department = scanner.nextLine();
            user = new com.library.model.user.Professor(UUID.randomUUID(), name, email, phoneNumber, department);
        } else {
            System.out.println("❌ Invalid user type. Please try again.");
            return;
        }

        userService.addUser(user);
        System.out.println("✅ User added successfully.");
    }

    private void addDocument() {
        System.out.print("Enter document title: ");
        String title = scanner.nextLine();
        if (!InputValidator.isValidTitle(title)) {
            System.out.println("❌ Invalid title. Please try again.");
            return;
        }
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter publication date (dd-MM-yyyy): ");
        String publicationDateStr = scanner.nextLine();
        if (!DateUtils.isValidDate(publicationDateStr)) {
            System.out.println("❌ Invalid date format. Please try again.");
            return;
        }
        LocalDate publicationDate = DateUtils.parseDate(publicationDateStr);
        int publicationYear = publicationDate.getYear();
        System.out.print("Enter document type (Book/Magazine/ScientificJournal/UniversityThesis): ");
        String type = scanner.nextLine();

        Document document;
        switch (type.toLowerCase()) {
            case "book":
                document = new com.library.model.document.Book(UUID.randomUUID(), title, author, publisher, publicationYear);
                break;
            case "magazine":
                document = new com.library.model.document.Magazine(UUID.randomUUID(), title, author, publisher, publicationYear);
                break;
            case "scientificjournal":
                System.out.print("Enter research field: ");
                String researchField = scanner.nextLine();
                document = new com.library.model.document.ScientificJournal(UUID.randomUUID(), title, author, publisher, publicationYear, researchField);
                break;
            case "universitythesis":
                System.out.print("Enter university: ");
                String university = scanner.nextLine();
                System.out.print("Enter field: ");
                String field = scanner.nextLine();
                document = new com.library.model.document.UniversityThesis(UUID.randomUUID(), title, author, publisher, publicationYear, university, field);
                break;
            default:
                System.out.println("❌ Invalid document type. Please try again.");
                return;
        }

        libraryService.addDocument(document);
        System.out.println("✅ Document added successfully.");
    }

    private void reserveDocument() {
        System.out.print("Enter user name: ");
        String userName = scanner.nextLine();
        System.out.print("Enter document title: ");
        String documentTitle = scanner.nextLine();
    
        User user = userService.findUserByName(userName).orElse(null);
        Document document = libraryService.findDocumentByTitle(documentTitle).orElse(null);
    
        if (user == null || document == null) {
            System.out.println("❌ User or document not found. Please try again.");
            return;
        }
    
        Reservation reservation = new Reservation(UUID.randomUUID(), document.getId(), user.getId(), LocalDate.now());
        boolean reserved = reservationService.reserveDocument(reservation);
    
        if (reserved) {
            System.out.println("✅ Document reserved successfully.");
        } else {
            System.out.println("❌ Unable to reserve the document. It may not be loaned or you may already have a reservation for it.");
        }
    }
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
    
        User user = userService.findUserByName(userName).orElse(null);
        Document document = libraryService.findDocumentByTitle(documentTitle).orElse(null);
    
        if (user == null || document == null) {
            System.out.println("❌ User or document not found. Please try again.");
            return;
        }
    
        if (loanService.isDocumentLoaned(document.getId())) {
            System.out.println("❌ This document is already loaned. Please try another document.");
            return;
        }
    
        Loan loan = new Loan(UUID.randomUUID(), document.getId(), user.getId(), loanDate, null);
        loanService.borrowDocument(loan);
        System.out.println("✅ Document loaned successfully.");
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
    
        User user = userService.findUserByName(userName).orElse(null);
        Document document = libraryService.findDocumentByTitle(documentTitle).orElse(null);
    
        if (user == null || document == null) {
            System.out.println("❌ User or document not found. Please try again.");
            return;
        }
    
        Loan loan = loanService.findLoanByUserAndDocument(user.getId(), document.getId()).orElse(null);
        if (loan == null) {
            System.out.println("❌ No active loan found for this user and document.");
            return;
        }
    
        loanService.returnDocument(loan.getId(), returnDate);
        System.out.println("✅ Document returned successfully.");
    }
 private void searchDocuments() {
    System.out.print("Enter search term: ");
    String searchTerm = scanner.nextLine();
    List<Document> documents = libraryService.getAllDocuments().stream()
        .filter(doc -> doc.getTitle().toLowerCase().contains(searchTerm.toLowerCase()))
        .collect(Collectors.toList());
    if (documents.isEmpty()) {
        System.out.println("❌ No documents found.");
    } else {
        System.out.println("📚 Found documents:");
        for (Document doc : documents) {
            System.out.println("📖 " + doc.getTitle() + " - ✍️ " + doc.getAuthor() + " (🏷️ " + doc.getType() + ")");
        }
    }
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
            System.out.println("🏷️ Type: " + user.getType());
            if (user instanceof Student) {
                System.out.println("🎓 Student ID: " + ((Student) user).getStudentId());
            } else if (user instanceof Professor) {
                System.out.println("🏫 Department: " + ((Professor) user).getDepartment());
            }
        }
        System.out.println("-----------------------------------");
    }
}

private void listAllDocuments() {
    List<Document> documents = libraryService.getAllDocuments();
    if (documents.isEmpty()) {
        System.out.println("❌ No documents found.");
    } else {
        System.out.println("📚 All documents:");
        for (Document doc : documents) {
            System.out.println("-----------------------------------");
            System.out.println("📖 Title: " + doc.getTitle());
            System.out.println("✍️ Author: " + doc.getAuthor());
            System.out.println("🏢 Publisher: " + doc.getPublisher());
            System.out.println("📅 Publication Year: " + doc.getPublicationYear());
            System.out.println("🏷️ Type: " + doc.getType());
            if (doc instanceof ScientificJournal) {
                System.out.println("🔬 Research Field: " + ((ScientificJournal) doc).getResearchField());
            } else if (doc instanceof UniversityThesis) {
                System.out.println("🎓 University: " + ((UniversityThesis) doc).getUniversity());
                System.out.println("📚 Field: " + ((UniversityThesis) doc).getField());
            }
            boolean isLoaned = loanService.isDocumentLoaned(doc.getId());
            System.out.println("📊 Status: " + (isLoaned ? "🔴 Loaned" : "🟢 Available"));
            
            List<UUID> reservingUsers = reservationService.getUsersReservingDocument(doc.getId());
            if (!reservingUsers.isEmpty()) {
                System.out.println("🔖 Reserved by:");
                for (UUID userId : reservingUsers) {
                    User user = userService.findUserById(userId).orElse(null);
                    if (user != null) {
                        System.out.println("   👤 " + user.getName());
                    }
                }
            }
        }
        System.out.println("-----------------------------------");
    }
}
}