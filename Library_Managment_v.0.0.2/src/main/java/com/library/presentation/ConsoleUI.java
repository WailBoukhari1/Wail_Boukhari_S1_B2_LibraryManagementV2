package com.library.presentation;

import com.library.business.Library;
import com.library.business.document.*;
import com.library.business.user.*;
import com.library.util.DateUtils;
import com.library.util.InputValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private Library library;
    private Scanner scanner;

    public ConsoleUI(Library library) {
        this.library = library;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1:
                    addDocument();
                    break;
                case 2:
                    borrowDocument();
                    break;
                case 3:
                    returnDocument();
                    break;
                case 4:
                    reserveDocument();
                    break;
                case 5:
                    cancelReservation();
                    break;
                case 6:
                    searchDocuments();
                    break;
                case 7:
                    addUser();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║    Library Management System       ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ 1. Add Document                    ║");
        System.out.println("║ 2. Borrow Document                 ║");
        System.out.println("║ 3. Return Document                 ║");
        System.out.println("║ 4. Reserve Document                ║");
        System.out.println("║ 5. Cancel Reservation              ║");
        System.out.println("║ 6. Search Documents                ║");
        System.out.println("║ 7. Add User                        ║");
        System.out.println("║ 0. Exit                            ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.print("Enter your choice: ");
    }

    private void addDocument() {
        System.out.println("\n┌─ Add Document ─────────────────────┐");
        System.out.println("│ 1. Book                            │");
        System.out.println("│ 2. Magazine                        │");
        System.out.println("│ 3. Scientific Journal              │");
        System.out.println("│ 4. University Thesis               │");
        System.out.println("└────────────────────────────────────┘");
        System.out.print("Enter document type: ");
        int type = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter publication date (YYYY-MM-DD): ");
        LocalDate publicationDate = DateUtils.parseDate(scanner.nextLine());

        Document document;
        switch (type) {
            case 1:
                System.out.print("Enter ISBN: ");
                String isbn = scanner.nextLine();
                System.out.print("Enter number of pages: ");
                int pages = scanner.nextInt();
                document = new Book(id, title, author, publicationDate, isbn, pages);
                break;
            case 2:
                System.out.print("Enter ISSN: ");
                String issn = scanner.nextLine();
                System.out.print("Enter issue number: ");
                int issueNumber = scanner.nextInt();
                document = new Magazine(id, title, author, publicationDate, issn, issueNumber);
                break;
            case 3:
                System.out.print("Enter ISSN: ");
                String journalIssn = scanner.nextLine();
                System.out.print("Enter research field: ");
                String researchField = scanner.nextLine();
                document = new ScientificJournal(id, title, author, publicationDate, journalIssn, researchField);
                break;
            case 4:
                System.out.print("Enter university: ");
                String university = scanner.nextLine();
                System.out.print("Enter degree: ");
                String degree = scanner.nextLine();
                document = new UniversityThesis(id, title, author, publicationDate, university, degree);
                break;
            default:
                System.out.println("Invalid document type.");
                return;
        }

        library.addDocument(document);
        System.out.println("\n✅ Document added successfully.");
    }

    private void borrowDocument() {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter document ID: ");
        String documentId = scanner.nextLine();

        library.borrowDocument(userId, documentId);
        System.out.println("\n✅ Document borrowed successfully.");
    }

    private void returnDocument() {
        System.out.print("Enter document ID: ");
        String documentId = scanner.nextLine();

        library.returnDocument(documentId);
        System.out.println("\n✅ Document returned successfully.");
    }

    private void reserveDocument() {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter document ID: ");
        String documentId = scanner.nextLine();

        library.reserveDocument(userId, documentId);
        System.out.println("\n✅ Document reserved successfully.");
    }

    private void cancelReservation() {
        System.out.print("Enter document ID: ");
        String documentId = scanner.nextLine();

        library.cancelReservation(documentId);
        System.out.println("\n✅ Reservation cancelled successfully.");
    }

    private void searchDocuments() {
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine();

        List<Document> results = library.searchDocuments(keyword);
        if (results.isEmpty()) {
            System.out.println("\n❌ No documents found.");
        } else {
            System.out.println("\n┌─ Search Results ─────────────────────┐");
            for (int i = 0; i < results.size(); i++) {
                Document doc = results.get(i);
                System.out.println("│ " + (i + 1) + ". " + formatDocumentInfo(doc));
            }
            System.out.println("└────────────────────────────────────┘");
        }
    }

    private String formatDocumentInfo(Document doc) {
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getTitle()).append(" by ").append(doc.getAuthor());
        sb.append(" (").append(doc.getPublicationDate()).append(")");

        if (doc instanceof Book) {
            Book book = (Book) doc;
            sb.append(" - ISBN: ").append(book.getIsbn());
        } else if (doc instanceof Magazine) {
            Magazine magazine = (Magazine) doc;
            sb.append(" - ISSN: ").append(magazine.getIssn());
        } else if (doc instanceof ScientificJournal) {
            ScientificJournal journal = (ScientificJournal) doc;
            sb.append(" - ISSN: ").append(journal.getIssn());
        } else if (doc instanceof UniversityThesis) {
            UniversityThesis thesis = (UniversityThesis) doc;
            sb.append(" - University: ").append(thesis.getUniversity());
        }

        return sb.toString();
    }

    private void addUser() {
        System.out.println("\n┌─ Add User ──────────────────────────┐");
        System.out.println("│ 1. Student                         │");
        System.out.println("│ 2. Professor                       │");
        System.out.println("└────────────────────────────────────┘");
        System.out.print("Enter user type: ");
        int type = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        if (!InputValidator.isValidEmail(email)) {
            System.out.println("Invalid email address.");
            return;
        }

        User user;
        switch (type) {
            case 1:
                System.out.print("Enter student ID: ");
                String studentId = scanner.nextLine();
                System.out.print("Enter major: ");
                String major = scanner.nextLine();
                user = new Student(id, name, email, studentId, major);
                break;
            case 2:
                System.out.print("Enter department: ");
                String department = scanner.nextLine();
                System.out.print("Enter research area: ");
                String researchArea = scanner.nextLine();
                user = new Professor(id, name, email, department, researchArea);
                break;
            default:
                System.out.println("Invalid user type.");
                return;
        }

        library.addUser(user);
        System.out.println("\n✅ User added successfully.");
    }
}