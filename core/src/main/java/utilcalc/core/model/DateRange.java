package utilcalc.core.model;

import static utilcalc.core.utils.Util.ensureNonNull;
import static utilcalc.core.utils.Util.ensureValidDateRange;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

public record DateRange(LocalDate startDate, LocalDate endDateExclusive) implements Comparable<DateRange> {
	public DateRange {
		ensureNonNull(startDate, "startDate");
		ensureNonNull(endDateExclusive, "endDateExclusive");
		ensureValidDateRange(startDate, endDateExclusive);

		if (startDate.equals(endDateExclusive)) {
			throw new IllegalArgumentException("Date range must not have same start and end date");
		}
	}

	public static DateRange fromInclusive(LocalDate startDateInclusive, LocalDate endDateInclusive) {
		return new DateRange(startDateInclusive, endDateInclusive.plusDays(1));
	}

	public Optional<DateRange> intersect(DateRange other) {
		LocalDate otherStartDate = other.startDate;
		LocalDate otherEndDate = other.endDateExclusive;

		LocalDate overlapStart = otherStartDate.isAfter(startDate) ? otherStartDate : startDate;
		LocalDate overlapEnd = otherEndDate.isBefore(endDateExclusive) ? otherEndDate : endDateExclusive;

		return overlapStart.isBefore(overlapEnd)
				? Optional.of(new DateRange(overlapStart, overlapEnd))
				: Optional.empty();
	}

	public BigDecimal getMonthCount() {

		if (isSingleMonth()) {
			return calculateDaysInMonthFraction(startDate, endDateExclusive);
		}

		return splitByMonths().stream().map(DateRange::getMonthCount).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public List<DateRange> splitByMonths() {
		List<DateRange> result = new ArrayList<>();
		LocalDate currentStart = startDate;

		while (currentStart.isBefore(endDateExclusive)) {
			YearMonth currentYM = YearMonth.from(currentStart);
			LocalDate monthEnd = currentYM.plusMonths(1).atDay(1);
			LocalDate currentEnd = monthEnd.isBefore(endDateExclusive) ? monthEnd : endDateExclusive;

			result.add(new DateRange(currentStart, currentEnd));
			currentStart = currentEnd;
		}

		return result;
	}

	private static BigDecimal calculateDaysInMonthFraction(LocalDate from, LocalDate toExclusive) {
		int daysInMonth = YearMonth.from(from).lengthOfMonth();
		long days = ChronoUnit.DAYS.between(from, toExclusive);
		return BigDecimal.valueOf(days).divide(BigDecimal.valueOf(daysInMonth), 10, RoundingMode.HALF_UP);
	}

	public boolean isSingleMonth() {
		return YearMonth.from(startDate).equals(YearMonth.from(endDateExclusive.minusDays(1)));
	}

	@Override
	public int compareTo(DateRange other) {
		return this.startDate.compareTo(other.startDate);
	}

	public Stream<LocalDate> stream() {
		return Stream.of(startDate, endDateExclusive);
	}
}
