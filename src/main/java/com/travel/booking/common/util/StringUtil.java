package com.travel.booking.common.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Component
public class StringUtil {
    private StringUtil() {
        /* This utility class should not be instantiated */
    }

    public static String formattingStringLikeParams(String param) {
        return "%" + param.toLowerCase() + "%";
    }

    public static String formattingPrice(BigDecimal price) {
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.of("id", "ID"));
        return currency.format(price);
    }
}
