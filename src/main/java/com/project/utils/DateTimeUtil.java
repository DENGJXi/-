package com.project.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtil {
    private DateTimeUtil() {}

    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 格式化 LocalDateTime -> String
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DEFAULT_DATETIME_FORMATTER);
    }

    public static String formatLocalDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null || pattern == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    // 解析 String -> LocalDateTime
    public static LocalDateTime parseLocalDateTime(String dateTimeStr) {
        if (dateTimeStr == null) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DEFAULT_DATETIME_FORMATTER);
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeStr, String pattern) {
        if (dateTimeStr == null || pattern == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateTimeStr, formatter);
    }
}
