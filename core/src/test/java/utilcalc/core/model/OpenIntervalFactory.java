package utilcalc.core.model;

import java.time.LocalDate;

public final class OpenIntervalFactory {
    private static final LocalDate JAN_01_2024 = LocalDate.of(2024, 1, 1);
    private static final LocalDate JAN_02_2024 = LocalDate.of(2024, 1, 2);
    private static final LocalDate JAN_15_2024 = LocalDate.of(2024, 1, 15);
    private static final LocalDate JAN_25_2024 = LocalDate.of(2024, 1, 25);

    private static final LocalDate JAN_01_2025 = LocalDate.of(2025, 1, 1);
    private static final LocalDate JAN_15_2025 = LocalDate.of(2025, 1, 15);

    private static final LocalDate JAN_01_2026 = LocalDate.of(2026, 1, 1);

    private OpenIntervalFactory() {}

    /** [ 1.1.2024 ; 1.1.2025 ) */
    public static OpenInterval full2024Interval() {
        return new OpenInterval(JAN_01_2024, JAN_01_2025);
    }

    /** [ 1.1.2025 ; 1.1.2026 ) */
    public static OpenInterval full2025Interval() {
        return new OpenInterval(JAN_01_2025, JAN_01_2026);
    }

    /** [ 15.1.2024 ; 25.1.2024 ) */
    public static OpenInterval partialMonthInterval() {
        return new OpenInterval(JAN_15_2024, JAN_25_2024);
    }

    /** [ 15.1.2024 ; 15.1.2025 ) */
    public static OpenInterval twoPartialMonthsInterval() {
        return new OpenInterval(JAN_15_2024, JAN_15_2025);
    }

    /** [ 1.1.2024 ; 2.1.2024 ) */
    public static OpenInterval oneDayInterval() {
        return new OpenInterval(JAN_01_2024, JAN_02_2024);
    }

    public static void emptyInterval() {
        new OpenInterval(JAN_15_2024, JAN_15_2024);
    }

    public static void reverseInterval() {
        new OpenInterval(JAN_15_2024, JAN_01_2024);
    }
}
