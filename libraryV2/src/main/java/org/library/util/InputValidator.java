package org.library.util;

import java.util.regex.Pattern;

public class InputValidator {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String ISBN_REGEX = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";
    private static final String ISSN_REGEX = "^\\d{4}-\\d{3}[\\dX]$";

    public static boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isValidISBN(String isbn) {
        return Pattern.matches(ISBN_REGEX, isbn);
    }

    public static boolean isValidISSN(String issn) {
        return Pattern.matches(ISSN_REGEX, issn);
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 100;
    }

    public static boolean isValidTitle(String title) {
        return title != null && !title.trim().isEmpty() && title.length() <= 200;
    }

    public static boolean isValidPageCount(int pageCount) {
        return pageCount > 0 && pageCount <= 10000;
    }

    public static boolean isValidIssueNumber(int issueNumber) {
        return issueNumber > 0;
    }

    public static boolean isValidUniversity(String university) {
        return university != null && !university.trim().isEmpty() && university.length() <= 100;
    }

    public static boolean isValidDomain(String domain) {
        return domain != null && !domain.trim().isEmpty() && domain.length() <= 50;
    }

    public static boolean isValidStudentId(String studentId) {
        return studentId != null && !studentId.trim().isEmpty() && studentId.length() <= 20;
    }

    public static boolean isValidEmployeeId(String employeeId) {
        return employeeId != null && !employeeId.trim().isEmpty() && employeeId.length() <= 20;
    }
}
