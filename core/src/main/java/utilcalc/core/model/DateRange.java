package utilcalc.core.model;

import static utilcalc.core.utils.Util.ensureNonNull;
import static utilcalc.core.utils.Util.ensureValidDateRange;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
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
        return splitIntoMonthFractions().values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(10, RoundingMode.HALF_UP);
    }

    public Map<YearMonth, BigDecimal> getCountsByMonth() {
        return splitIntoMonthFractions();
    }

    private Map<YearMonth, BigDecimal> splitIntoMonthFractions() {
        Map<YearMonth, BigDecimal> result = new HashMap<>();
        YearMonth startMonth = YearMonth.from(startDate);
        YearMonth endMonth = YearMonth.from(endDateExclusive);

        if (startMonth.equals(endMonth)) {
            result.put(startMonth, calculateDaysInMonthFraction(startDate, endDateExclusive));
            return result;
        }

        BigDecimal firstMonthCount =
                startDate.equals(startMonth.atDay(1))
                        ? BigDecimal.ONE
                        : calculateDaysInMonthFraction(
                                startDate, startMonth.atEndOfMonth().plusDays(1));

        result.put(startMonth, firstMonthCount);

        YearMonth current = startMonth.plusMonths(1);
        while (current.isBefore(endMonth)) {
            result.put(current, BigDecimal.ONE);
            current = current.plusMonths(1);
        }

        if (!endDateExclusive.equals(endMonth.atDay(1))) {
            result.put(endMonth, calculateDaysInMonthFraction(endMonth.atDay(1), endDateExclusive));
        }

        return result;
    }

    private static BigDecimal calculateDaysInMonthFraction(LocalDate from, LocalDate toExclusive) {
        int daysInMonth = YearMonth.from(from).lengthOfMonth();
        long days = ChronoUnit.DAYS.between(from, toExclusive);
        return BigDecimal.valueOf(days)
                .divide(BigDecimal.valueOf(daysInMonth), 10, RoundingMode.HALF_UP);
    }
}
