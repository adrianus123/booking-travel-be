package com.travel.booking.common.util;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {
    private StringUtil() {
        /* This utility class should not be instantiated */
    }

    public static String formattingStringLikeParams(String param) {
        return "%" + param.toLowerCase() + "%";
    }
}
