package utilcalc.core.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class DateRangeTest {

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
