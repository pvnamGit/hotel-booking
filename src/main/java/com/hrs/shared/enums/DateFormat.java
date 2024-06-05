package com.hrs.shared.enums;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public enum DateFormat {
    // Define common date formats
    DATE_FORMAT_DD_MM_YYYY("dd-MM-yyyy"),
    DATE_FORMAT_MM_DD_YYYY("MM-dd-yyyy"),
    DATE_FORMAT_YYYY_MM_DD("yyyy-MM-dd"),
    DATE_TIME_FORMAT_DD_MM_YYYY_HH_MM_SS("dd-MM-yyyy HH:mm:ss"),
    DATE_TIME_FORMAT_YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss");

    private final String pattern;

    DateFormat(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern(pattern);
    }

    public static LocalDate parseDate(String date, DateFormat dateFormat) {
        return LocalDate.parse(date, dateFormat.getFormatter());
    }

    public static String formatDate(LocalDate date, DateFormat dateFormat) {
        return date.format(dateFormat.getFormatter());
    }

    public static LocalDateTime parseDateTime(String dateTime, DateFormat dateFormat) {
        return LocalDateTime.parse(dateTime, dateFormat.getFormatter());
    }

    public static String formatDateTime(LocalDateTime dateTime, DateFormat dateFormat) {
        return dateTime.format(dateFormat.getFormatter());
    }
}
