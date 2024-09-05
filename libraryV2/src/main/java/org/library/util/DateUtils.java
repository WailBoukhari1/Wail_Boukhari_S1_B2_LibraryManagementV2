package org.library.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use " + DATE_FORMAT);
        }
    }

    public static String formatDate(LocalDate date) {
        return date.format(formatter);
    }

    public static boolean isValidDate(String dateString) {
        try {
            LocalDate.parse(dateString, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public static boolean isDateInFuture(LocalDate date) {
        return date.isAfter(getCurrentDate());
    }

    public static boolean isDateInPast(LocalDate date) {
        return date.isBefore(getCurrentDate());
    }

    public static long daysBetween(LocalDate start, LocalDate end) {
        return java.time.temporal.ChronoUnit.DAYS.between(start, end);
    }
}
