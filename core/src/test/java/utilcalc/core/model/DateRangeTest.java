package utilcalc.core.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class DateRangeTest {

    @Test
    void overlapSameRange_should_returnListOfDateRange() {
        DateRange dateRange = createInterval("2024-01-01", "2025-01-01");
        DateRange other = createInterval("2024-01-01", "2025-01-01");

        dateRange
                .intersect(other)
                .ifPresent(
                        range -> assertEquals(createInterval("2024-01-01", "2025-01-01"), range));
    }

    @Test
    void longerRange_should_returnListOfDateRange() {
        DateRange dateRange = createInterval("2023-01-01", "2026-01-01");
        DateRange other = createInterval("2024-01-01", "2025-01-01");

        dateRange
                .intersect(other)
                .ifPresent(
                        range -> assertEquals(createInterval("2024-01-01", "2025-01-01"), range));
    }

    @Test
    void longerInputRange_should_returnListOfDateRange() {
        DateRange dateRange = createInterval("2024-01-01", "2025-01-01");
        DateRange other = createInterval("2023-01-01", "2026-01-01");

        dateRange
                .intersect(other)
                .ifPresent(
                        range -> assertEquals(createInterval("2024-01-01", "2025-01-01"), range));
    }

    @Test
    void oneDayCoverageRange_should_returnListOfDateRange() {
        DateRange dateRange = createInterval("2024-01-01", "2024-01-15");
        DateRange other = createInterval("2024-01-14", "2026-01-01");

        dateRange
                .intersect(other)
                .ifPresent(
                        range -> assertEquals(createInterval("2024-01-14", "2024-01-15"), range));
    }

    @Test
    void notCoverageRange_should_returnEmptyListOfDateRange() {
        DateRange dateRange = createInterval("2024-01-01", "2025-01-01");
        DateRange other = createInterval("2026-01-01", "2027-01-01");

        assertTrue(dateRange.intersect(other).isEmpty());
    }

    @Test
    void full2024Interval_should_haveCorrectMonthCount() {
        assertThat(createInterval("2024-01-01", "2025-01-01").getMonthCount())
                .isEqualTo("12.0000000000");
    }

    @Test
    void partialMonthInterval_should_haveCorrectMonthCount() {
        assertThat(createInterval("2024-01-15", "2024-01-25").getMonthCount())
                .isEqualTo("0.3225806452");
    }

    @Test
    void twoPartialMonthsInterval_should_haveCorrectMonthCount() {
        assertThat(createInterval("2024-01-15", "2025-01-15").getMonthCount())
                .isEqualTo("12.0000000000");
    }

    @Test
    void oneDayInterval_should_haveCorrectMonthCount() {
        assertThat(createInterval("2024-01-01", "2024-01-02").getMonthCount())
                .isEqualTo("0.0322580645");
    }

    @Test
    void full2024Interval_should_haveCorrectCountsByMonth() {
        Map<YearMonth, BigDecimal> countsByMonth =
                Map.ofEntries(
                        Map.entry(YearMonth.parse("2024-01"), new BigDecimal("1.0000000000")),
                        Map.entry(YearMonth.parse("2024-02"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-03"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-04"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-05"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-06"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-07"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-08"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-09"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-10"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-11"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-12"), BigDecimal.ONE));

        assertThat(createInterval("2024-01-01", "2025-01-01").getCountsByMonth())
                .isEqualTo(countsByMonth);
    }

    @Test
    void partialMonthInterval_should_haveCorrectCountsByMonth() {
        Map<YearMonth, BigDecimal> countsByMonth =
                Map.ofEntries(
                        Map.entry(YearMonth.parse("2024-01"), new BigDecimal("0.3225806452")));
        assertThat(createInterval("2024-01-15", "2024-01-25").getCountsByMonth())
                .isEqualTo(countsByMonth);
    }

    @Test
    void twoPartialMonthsInterval_should_haveCorrectCountsByMonth() {
        Map<YearMonth, BigDecimal> countsByMonth =
                Map.ofEntries(
                        Map.entry(YearMonth.parse("2024-01"), new BigDecimal("0.5483870968")),
                        Map.entry(YearMonth.parse("2024-02"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-03"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-04"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-05"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-06"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-07"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-08"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-09"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-10"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-11"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2024-12"), BigDecimal.ONE),
                        Map.entry(YearMonth.parse("2025-01"), new BigDecimal("0.4516129032")));
        assertThat(createInterval("2024-01-15", "2025-01-15").getCountsByMonth())
                .isEqualTo(countsByMonth);
    }

    @Test
    void oneDayInterval_should_haveCorrectCountsByMonth() {
        Map<YearMonth, BigDecimal> countsByMonth =
                Map.ofEntries(
                        Map.entry(YearMonth.parse("2024-01"), new BigDecimal("0.0322580645")));
        assertThat(createInterval("2024-01-01", "2024-01-02").getCountsByMonth())
                .isEqualTo(countsByMonth);
    }

    @Test
    void emptyInterval_should_throw_illegalArgumentException() {
        assertThatThrownBy(() -> createInterval("2024-01-15", "2024-01-15"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Date range must not have same start and end date");
    }

    @Test
    void reverseInterval_should_throw_illegalArgumentException() {
        assertThatThrownBy(() -> createInterval("2024-01-15", "2024-01-01"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("endDate must not be before startDate");
    }

    private static DateRange createInterval(String start, String end) {
        return new DateRange(LocalDate.parse(start), LocalDate.parse(end));
    }
}
