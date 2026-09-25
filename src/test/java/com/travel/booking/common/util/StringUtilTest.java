package com.travel.booking.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilTest {

    @Test
    @DisplayName("formattingStringLikeParams - Mengubah parameter LIKE menjadi huruf kecil")
    void formattingStringLikeParams_ConvertsToLowercaseWithWildcards() {
        String result = StringUtil.formattingStringLikeParams("JaKaRtA");

        assertThat(result).isEqualTo("%jakarta%");
    }

    @Test
    @DisplayName("formattingPrice - Memformat harga sebagai mata uang Indonesia")
    void formattingPrice_FormatsIndonesianCurrency() {
        String result = StringUtil.formattingPrice(new BigDecimal("150000"));

        assertThat(result).isEqualTo("Rp150.000,00");
    }
}
