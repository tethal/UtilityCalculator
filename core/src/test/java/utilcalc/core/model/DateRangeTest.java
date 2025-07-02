package utilcalc.core.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class DateRangeTest {

    @Test
    void overlapSameRange_should_returnOverlapDateRange() {
        DateRange dateRange = createInterval("2024-01-01", "2025-01-01");
        DateRange other = createInterval("2024-01-01", "2025-01-01");

        Optional<DateRange> result = dateRange.intersect(other);

        assertTrue(result.isPresent(), "Expected intersect() to return a non-empty Optional");
        assertThat(result.get()).isEqualTo(createInterval("2024-01-01", "2025-01-01"));
    }

    @Test
    void longerRange_should_returnOverlapDateRange() {
        DateRange dateRange = createInterval("2023-01-01", "2026-01-01");
        DateRange other = createInterval("2024-01-01", "2025-01-01");

        Optional<DateRange> result = dateRange.intersect(other);

        assertTrue(result.isPresent(), "Expected intersect() to return a non-empty Optional");
        assertThat(result.get()).isEqualTo(createInterval("2024-01-01", "2025-01-01"));
    }

    @Test
    void longerInputRange_should_returnOverlapDateRange() {
        DateRange dateRange = createInterval("2024-01-01", "2025-01-01");
        DateRange other = createInterval("2023-01-01", "2026-01-01");

        Optional<DateRange> result = dateRange.intersect(other);

        assertTrue(result.isPresent(), "Expected intersect() to return a non-empty Optional");
        assertThat(result.get()).isEqualTo(createInterval("2024-01-01", "2025-01-01"));
    }

    @Test
    void oneDayCoverageRange_should_returnOverlapDateRange() {
        DateRange dateRange = createInterval("2024-01-01", "2024-01-15");
        DateRange other = createInterval("2024-01-14", "2026-01-01");

        Optional<DateRange> result = dateRange.intersect(other);

        assertTrue(result.isPresent(), "Expected intersect() to return a non-empty Optional");
        assertThat(result.get()).isEqualTo(createInterval("2024-01-14", "2024-01-15"));
    }

    @Test
    void notCoverageRange_should_returnEmptyDateRange() {
        DateRange dateRange = createInterval("2024-01-01", "2025-01-01");
        DateRange other = createInterval("2026-01-01", "2027-01-01");

        assertTrue(dateRange.intersect(other).isEmpty());
    }

    @Test
    void full2024Interval_should_haveCorrectMonthCount() {
        assertThat(createInterval("2024-01-01", "2025-01-01").getMonthCount()).isEqualTo("12");
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
