package utilcalc.core.model;

import static utilcalc.core.utils.Util.ensureNonNull;
import static utilcalc.core.utils.Util.ensureValidDateRange;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

public record OpenInterval(LocalDate startDate, LocalDate endDateExclusive) {
    public OpenInterval {
        ensureNonNull(startDate, "startDate");
        ensureNonNull(endDateExclusive, "endDateExclusive");
        ensureValidDateRange(startDate, endDateExclusive);

        if (startDate.equals(endDateExclusive)) {
            throw new IllegalArgumentException(
                    "Open interval must not have same start and end date");
        }
    }

    public BigDecimal getMonthCount() {
        YearMonth startMonth = YearMonth.from(startDate);
        YearMonth endMonth = YearMonth.from(endDateExclusive);

        if (startMonth.equals(endMonth)) {
            return calculateDaysInMonthFraction(startDate, endDateExclusive);
        }

        LocalDate startMonthEnd = startMonth.atEndOfMonth().plusDays(1);
        BigDecimal partialStart = calculateDaysInMonthFraction(startDate, startMonthEnd);

        LocalDate endMonthStart = endMonth.atDay(1);
        BigDecimal partialEnd = calculateDaysInMonthFraction(endMonthStart, endDateExclusive);

        long fullMonths =
                ChronoUnit.MONTHS.between(
                        YearMonth.from(startMonthEnd), YearMonth.from(endMonthStart));

        return BigDecimal.valueOf(fullMonths)
                .add(partialStart)
                .add(partialEnd)
                .setScale(10, RoundingMode.HALF_UP);
    }

    private static BigDecimal calculateDaysInMonthFraction(LocalDate from, LocalDate toExclusive) {
        int daysInMonth = YearMonth.from(from).lengthOfMonth();
        long days = ChronoUnit.DAYS.between(from, toExclusive);
        return BigDecimal.valueOf(days)
                .divide(BigDecimal.valueOf(daysInMonth), 10, RoundingMode.HALF_UP);
    }
}
