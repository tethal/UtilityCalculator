package utilcalc.core.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class ReportFormatter {

    private final DateTimeFormatter dateFormatter;

    public ReportFormatter() {
        this.dateFormatter = DateTimeFormatter.ofPattern("d. M. yyyy");
    }

    public String formatDate(LocalDate date) {
        return date != null ? date.format(dateFormatter) : "";
    }

    public String formatPeriod(LocalDate from, LocalDate to) {
        return formatDate(from) + " – " + formatDate(to);
    }
}
