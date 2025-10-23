package utilcalc.core.parser;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.regex.Pattern;
import utilcalc.core.model.DateRange;

class DateRangeParser {

    private static final String RANGE_SEPARATOR = "...";
    private static final Pattern RANGE_SPLIT_PATTERN = Pattern.compile("\\.\\.\\.");

    private static final Pattern YEAR_PATTERN = Pattern.compile("\\d{4}");
    private static final Pattern YEAR_MONTH_PATTERN = Pattern.compile("\\d{4}-\\d{2}");
    private static final Pattern YEAR_MONTH_DAY_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    private DateRangeParser() {}

    static DateRange parse(String text) {
        String normalizedText = text.strip();

        if (normalizedText.contains(RANGE_SEPARATOR)) {
            String[] parts = RANGE_SPLIT_PATTERN.split(normalizedText, 2);
            if (parts.length != 2) {
                throw new ParsingException("Invalid range format: " + normalizedText);
            }
            return parseInterval(parts[0].strip(), parts[1].strip());
        } else {
            return parseSingle(normalizedText);
        }
    }

    private static DateRange parseInterval(String from, String to) {
        if (YEAR_MONTH_DAY_PATTERN.matcher(from).matches()
                && YEAR_MONTH_DAY_PATTERN.matcher(to).matches()) {
            LocalDate start = LocalDate.parse(from);
            LocalDate end = LocalDate.parse(to).plusDays(1);
            return new DateRange(start, end);
        } else if (YEAR_MONTH_PATTERN.matcher(from).matches()
                && YEAR_MONTH_PATTERN.matcher(to).matches()) {
            LocalDate start = YearMonth.parse(from).atDay(1);
            LocalDate end = YearMonth.parse(to).atEndOfMonth().plusDays(1);
            return new DateRange(start, end);
        } else if (YEAR_PATTERN.matcher(from).matches() && YEAR_PATTERN.matcher(to).matches()) {
            LocalDate start = Year.parse(from).atDay(1);
            LocalDate end = Year.parse(to).atMonth(12).atEndOfMonth().plusDays(1);
            return new DateRange(start, end);
        } else {
            throw new ParsingException("Unsupported interval format: " + from + "..." + to);
        }
    }

    private static DateRange parseSingle(String text) {
        if (YEAR_MONTH_PATTERN.matcher(text).matches()) {
            LocalDate start = YearMonth.parse(text).atDay(1);
            LocalDate end = start.plusMonths(1);
            return new DateRange(start, end);
        } else if (YEAR_PATTERN.matcher(text).matches()) {
            LocalDate start = Year.parse(text).atDay(1);
            LocalDate end = start.plusYears(1);
            return new DateRange(start, end);
        } else {
            throw new ParsingException("Unsupported date format: " + text);
        }
    }
}
