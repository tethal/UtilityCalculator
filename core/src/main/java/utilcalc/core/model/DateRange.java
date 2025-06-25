package utilcalc.core.model;

import static utilcalc.core.utils.Util.ensureNonNull;
import static utilcalc.core.utils.Util.ensureValidDateRange;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public record DateRange(LocalDate startDate, LocalDate endDateExclusive) {
    public DateRange {
        ensureNonNull(startDate, "startDate");
        ensureNonNull(endDateExclusive, "endDateExclusive");
        ensureValidDateRange(startDate, endDateExclusive);

        if (startDate.equals(endDateExclusive)) {
            throw new IllegalArgumentException("Date range must not have same start and end date");
        }
    }

    public static DateRange fromInclusive(
            LocalDate startDateInclusive, LocalDate endDateInclusive) {
        return new DateRange(startDateInclusive, endDateInclusive.plusDays(1));
    }

    public Optional<DateRange> intersect(DateRange other) {
        LocalDate otherStartDate = other.startDate;
        LocalDate otherEndDate = other.endDateExclusive;

        LocalDate overlapStart = otherStartDate.isAfter(startDate) ? otherStartDate : startDate;
        LocalDate overlapEnd =
                otherEndDate.isBefore(endDateExclusive) ? otherEndDate : endDateExclusive;

        return overlapStart.isBefore(overlapEnd)
                ? Optional.of(new DateRange(overlapStart, overlapEnd))
                : Optional.empty();
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
