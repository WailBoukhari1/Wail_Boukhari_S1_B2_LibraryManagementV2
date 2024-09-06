package com.library.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate parseDate(String date) {
        return LocalDate.parse(date, formatter);
    }

    public static String formatDate(LocalDate date) {
        return date.format(formatter);
    }
}
