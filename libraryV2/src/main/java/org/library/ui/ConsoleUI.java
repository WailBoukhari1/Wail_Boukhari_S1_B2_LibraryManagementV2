package org.library.ui;

import org.library.model.*;
import org.library.service.Borrowable;
import org.library.service.Library;
import org.library.service.Reservable;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final Library library;
    private final Scanner scanner;

    public ConsoleUI(Library library) {
        this.library = library;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            clearScreen();
            displayMainMenu();
            int choice = getValidIntInput("Enter your choice: ", 0, 6);

            switch (choice) {
                case 1:
                    addUser();
                    break;
                case 2:
                    addDocument();
                    break;
                case 3:
                    borrowDocument();
                    break;
                case 4:
                    returnDocument();
                    break;
                case 5:
                    displayAllUsers();
                    break;
                case 6:
                    displayAllDocuments();
                    break;
                case 0:
                    running = false;
                    break;
            }

            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        System.out.println("Thank you for using the Library Management System!");
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void displayMainMenu() {
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║     Library Management System      ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ 1. Add User                        ║");
        System.out.println("║ 2. Add Document                    ║");
        System.out.println("║ 3. Borrow Document                 ║");
        System.out.println("║ 4. Return Document                 ║");
        System.out.println("║ 5. Display All Users               ║");
        System.out.println("║ 6. Display All Documents           ║");
        System.out.println("║ 0. Exit                            ║");
        System.out.println("╚════════════════════════════════════╝");
    }

    private void displayAllUsers() {
        System.out.println("\n=== All Users ===");
        List<User> users = library.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found in the library.");
        } else {
            for (User user : users) {
                System.out.println("┌─────────────────────────────────────");
                System.out.printf("│ ID: %-30s\n", user.getId());
                System.out.printf("│ Name: %-28s\n", user.getName());
                System.out.printf("│ Email: %-27s\n", user.getEmail());
                System.out.printf("│ Type: %-28s\n", user.getUserType());
                if (user instanceof Student student) {
                    System.out.printf("│ Student ID: %-23s\n", student.getStudentId());
                    System.out.printf("│ Major: %-27s\n", student.getMajor());
                } else if (user instanceof Professor professor) {
                    System.out.printf("│ Employee ID: %-22s\n", professor.getEmployeeId());
                    System.out.printf("│ Department: %-23s\n", professor.getDepartment());
                }
                System.out.println("└─────────────────────────────────────");
            }
        }
    }

    private void displayAllDocuments() {
        System.out.println("\n=== All Documents ===");
        List<Document> documents = library.getAllDocuments();
        if (documents.isEmpty()) {
            System.out.println("No documents found in the library.");
        } else {
            for (Document document : documents) {
                System.out.println("┌─────────────────────────────────────");
                System.out.printf("│ ID: %-30s\n", document.getId());
                System.out.printf("│ Title: %-27s\n", document.getTitle());
                System.out.printf("│ Author: %-26s\n", document.getAuthor());
                System.out.printf("│ Publication Date: %-17s\n", document.getPublicationDate());
                System.out.printf("│ Type: %-28s\n", document.getType());

                if (document instanceof Book book) {
                    System.out.printf("│ ISBN: %-28s\n", book.getIsbn());
                    System.out.printf("│ Page Count: %-23d\n", book.getPageCount());
                } else if (document instanceof Thesis thesis) {
                    System.out.printf("│ University: %-23s\n", thesis.getUniversity());
                    System.out.printf("│ Domain: %-26s\n", thesis.getDomain());
                } else if (document instanceof ScientificJournal journal) {
                    System.out.printf("│ ISSN: %-28s\n", journal.getIssn());
                    System.out.printf("│ Research Domain: %-19s\n", journal.getResearchDomain());
                } else if (document instanceof Magazine magazine) {
                    System.out.printf("│ ISSN: %-28s\n", magazine.getIssn());
                    System.out.printf("│ Issue Number: %-21d\n", magazine.getIssueNumber());
                }

                if (document instanceof Borrowable) {
                    System.out.printf("│ Borrowed: %-25s\n", ((Borrowable) document).isBorrowed() ? "Yes" : "No");
                }
                if (document instanceof Reservable) {
                    System.out.printf("│ Reserved: %-25s\n", ((Reservable) document).isReserved() ? "Yes" : "No");
                }
                System.out.println("└─────────────────────────────────────");
            }
        }
    }

    private int getValidIntInput(String prompt, int min, int max) {
        int input;
        do {
            System.out.print(prompt);
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                System.out.print(prompt);
            }
            input = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (input < min || input > max) {
                System.out.printf("Please enter a number between %d and %d.\n", min, max);
            }
        } while (input < min || input > max);
        return input;
    }

    private void addUser() {
        System.out.println("\n--- Add User ---");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter user type (Student/Professor): ");
        String userType = scanner.nextLine();

        User user;
        if (userType.equalsIgnoreCase("Student")) {
            System.out.print("Enter student ID: ");
            String studentId = scanner.nextLine();
            System.out.print("Enter major: ");
            String major = scanner.nextLine();
            user = new Student(name, email, studentId, major);
        } else if (userType.equalsIgnoreCase("Professor")) {
            System.out.print("Enter employee ID: ");
            String employeeId = scanner.nextLine();
            System.out.print("Enter department: ");
            String department = scanner.nextLine();
            user = new Professor(name, email, employeeId, department);
        } else {
            System.out.println("Invalid user type. User not added.");
            return;
        }

        library.addUser(user);
        System.out.println("User added successfully!");
    }

    private void addDocument() {
        System.out.println("\n--- Add Document ---");
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter publication date (YYYY-MM-DD): ");
        LocalDate publicationDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter document type (Book/Thesis/ScientificJournal/Magazine): ");
        String documentType = scanner.nextLine();

        Document document;
        switch (documentType.toLowerCase()) {
            case "book":
                System.out.print("Enter ISBN: ");
                String isbn = scanner.nextLine();
                System.out.print("Enter page count: ");
                int pageCount = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                document = new Book(title, author, publicationDate, isbn, pageCount);
                break;
            case "thesis":
                System.out.print("Enter university: ");
                String university = scanner.nextLine();
                System.out.print("Enter domain: ");
                String domain = scanner.nextLine();
                document = new Thesis(title, author, publicationDate, university, domain);
                break;
            case "scientificjournal":
                System.out.print("Enter ISSN: ");
                String issn = scanner.nextLine();
                System.out.print("Enter research domain: ");
                String researchDomain = scanner.nextLine();
                document = new ScientificJournal(title, author, publicationDate, issn, researchDomain);
                break;
            case "magazine":
                System.out.print("Enter ISSN: ");
                String magazineIssn = scanner.nextLine();
                System.out.print("Enter issue number: ");
                int issueNumber = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                document = new Magazine(title, author, publicationDate, magazineIssn, issueNumber);
                break;
            default:
                System.out.println("Invalid document type. Document not added.");
                return;
        }

        library.addDocument(document);
        System.out.println("Document added successfully!");
    }

    private void borrowDocument() {
        System.out.println("\n--- Borrow Document ---");
        System.out.print("Enter user ID: ");
        Long userId = scanner.nextLong();
        System.out.print("Enter document ID: ");
        Long documentId = scanner.nextLong();
        scanner.nextLine(); // Consume newline

        library.borrowDocument(userId, documentId);
    }

    private void returnDocument() {
        System.out.println("\n--- Return Document ---");
        System.out.print("Enter user ID: ");
        Long userId = scanner.nextLong();
        System.out.print("Enter document ID: ");
        Long documentId = scanner.nextLong();
        scanner.nextLine(); // Consume newline

        library.returnDocument(userId, documentId);
    }
}