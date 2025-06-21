package utilcalc.core.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static utilcalc.core.model.OpenIntervalFactory.*;

import org.junit.jupiter.api.Test;

public class OpenIntervalTest {

    @Test
    void full2024Interval_should_haveCorrectMonthCount() {
        assertThat(full2024Interval().getMonthCount()).isEqualTo("12.0000000000");
    }

    @Test
    void partialMonthInterval_should_haveCorrectMonthCount() {
        assertThat(partialMonthInterval().getMonthCount()).isEqualTo("0.3225806452");
    }

    @Test
    void twoPartialMonthsInterval_should_haveCorrectMonthCount() {
        assertThat(twoPartialMonthsInterval().getMonthCount()).isEqualTo("12.0000000000");
    }

    @Test
    void oneDayInterval_should_haveCorrectMonthCount() {
        assertThat(oneDayInterval().getMonthCount()).isEqualTo("0.0322580645");
    }

    @Test
    void emptyInterval_should_throw_illegalArgumentException() {
        assertThatThrownBy(OpenIntervalFactory::emptyInterval)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Open interval must not have same start and end date");
    }

    @Test
    void reverseInterval_should_throw_illegalArgumentException() {
        assertThatThrownBy(OpenIntervalFactory::reverseInterval)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("endDate must not be before startDate");
    }
}
