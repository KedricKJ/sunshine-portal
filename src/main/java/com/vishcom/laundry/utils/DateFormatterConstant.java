package com.vishcom.laundry.utils;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class DateFormatterConstant {

    public static String localDateTimeToString(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimePattern.STRICT_DATE_TIME_WITHOUT_ZONE.getPattern())
                .withResolverStyle(ResolverStyle.STRICT);
        return formatter.format(date);

    }

}
