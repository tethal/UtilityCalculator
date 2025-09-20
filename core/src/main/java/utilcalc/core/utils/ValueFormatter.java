package utilcalc.core.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Currency;
import java.util.Locale;
import utilcalc.core.model.DateRange;

public final class ValueFormatter {
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    private final DateTimeFormatter dateFormatter;
    private final NumberFormat numberFormat;
    private final NumberFormat moneyFormat;
    private final Locale locale;

    public ValueFormatter() {
        this(Locale.forLanguageTag("cs-CZ"), Currency.getInstance("CZK"));
    }

    public ValueFormatter(Locale locale) {
        this(locale, Currency.getInstance("CZK"));
    }

    public ValueFormatter(Locale locale, Currency currency) {
        this.locale = locale;
        this.dateFormatter = DateTimeFormatter.ofPattern("d. M. yyyy", locale);

        this.numberFormat = NumberFormat.getNumberInstance(locale);
        this.numberFormat.setMinimumFractionDigits(0);
        this.numberFormat.setMaximumFractionDigits(5);
        this.numberFormat.setGroupingUsed(true);

        this.moneyFormat = NumberFormat.getCurrencyInstance(locale);
        this.moneyFormat.setCurrency(currency);
        this.moneyFormat.setMinimumFractionDigits(2);
        this.moneyFormat.setMaximumFractionDigits(2);
    }

    public String formatDate(LocalDate date) {
        return date != null ? date.format(dateFormatter) : "";
    }

    public String formatPeriod(LocalDate from, LocalDate to) {
        return formatDate(from) + " – " + formatDate(to);
    }

    public String formatPeriod(DateRange dateRange) {
        if (dateRange == null) return "";
        return formatPeriod(dateRange.startDate(), dateRange.endDateExclusive().minusDays(1));
    }

    public String formatYearMonth(YearMonth ym) {
        if (ym == null) return "";
        String monthName = ym.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, locale);
        return monthName + " " + ym.getYear();
    }

    public String formatMoney(BigDecimal amount) {
        return amount != null ? moneyFormat.format(amount) : "";
    }

    public String formatNumber(BigDecimal number) {
        return number != null ? numberFormat.format(number) : "";
    }

    public String formatPercent(BigDecimal value) {
        return value.multiply(ONE_HUNDRED).intValueExact() + " %";
    }

    public String formatMonths(BigDecimal value) {
        String numberStr = formatNumber(value);
        if (value.abs().compareTo(BigDecimal.ONE) == 0) {
            return numberStr + " měsíc";
        }
        char lastChar = numberStr.charAt(numberStr.length() - 1);
        if (lastChar >= '2' && lastChar <= '4') {
            if (numberStr.length() >= 2) {
                char secondLastChar = numberStr.charAt(numberStr.length() - 2);
                if (secondLastChar != '1') {
                    return numberStr + " měsíce";
                }
            } else {
                return numberStr + " měsíce";
            }
        }
        return numberStr + " měsíců";
    }
}
