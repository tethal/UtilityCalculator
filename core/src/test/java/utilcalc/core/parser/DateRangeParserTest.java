package utilcalc.core.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import utilcalc.core.model.DateRange;

class DateRangeParserTest {

    @Test
    void testSingleMonth() {
        DateRange range = DateRangeParser.parse("2025-03");
        assertEquals(LocalDate.of(2025, 3, 1), range.startDate());
        assertEquals(LocalDate.of(2025, 4, 1), range.endDateExclusive());
    }

    @Test
    void testSingleYear() {
        DateRange range = DateRangeParser.parse("2025");
        assertEquals(LocalDate.of(2025, 1, 1), range.startDate());
        assertEquals(LocalDate.of(2026, 1, 1), range.endDateExclusive());
    }

    @Test
    void testIntervalDays() {
        DateRange range = DateRangeParser.parse("2025-03-04...2025-08-12");
        assertEquals(LocalDate.of(2025, 3, 4), range.startDate());
        assertEquals(LocalDate.of(2025, 8, 13), range.endDateExclusive());
    }

    @Test
    void testIntervalMonths() {
        DateRange range = DateRangeParser.parse("2025-03...2025-12");
        assertEquals(LocalDate.of(2025, 3, 1), range.startDate());
        assertEquals(LocalDate.of(2026, 1, 1), range.endDateExclusive());
    }

    @Test
    void testIntervalYears() {
        DateRange range = DateRangeParser.parse("2022...2023");
        assertEquals(LocalDate.of(2022, 1, 1), range.startDate());
        assertEquals(LocalDate.of(2024, 1, 1), range.endDateExclusive());
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "2025-02-30"})
    void testInvalidFormat(String invalidInput) {
        assertThrows(
                ParsingException.class,
                () -> DateRangeParser.parse(invalidInput),
                "Input should throw ParsingException: " + invalidInput);
    }
}
