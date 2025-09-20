package utilcalc.core.util;

import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import utilcalc.core.utils.ValueFormatter;

class ValueFormatterTest {

    ValueFormatter formatter = new ValueFormatter();

    @ParameterizedTest
    @CsvSource(
            delimiter = ';',
            value = {
                "1;1 měsíc",
                "2;2 měsíce",
                "12;12 měsíců",
                "22;22 měsíce",
                "32;32 měsíce",
                "112;112 měsíců",
                "1.0;1 měsíc",
                "1.2;1,2 měsíce",
                "1.3;1,3 měsíce",
                "1.4;1,4 měsíce",
                "1.5;1,5 měsíců",
                "1.12;1,12 měsíců",
                "1.13;1,13 měsíců",
                "1.14;1,14 měsíců",
                "1.3452;1,3452 měsíce",
                "2.22;2,22 měsíce",
                "3.112;3,112 měsíců"
            })
    void testMonths(String input, String expected) {
        Assertions.assertEquals(expected, formatter.formatMonths(new BigDecimal(input)));
    }
}
