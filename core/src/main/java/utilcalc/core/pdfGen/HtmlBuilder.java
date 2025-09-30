package utilcalc.core.pdfGen;

import java.math.BigDecimal;
import java.time.YearMonth;
import utilcalc.core.model.DateRange;
import utilcalc.core.utils.ValueFormatter;

public final class HtmlBuilder {

    private final StringBuilder sb = new StringBuilder();
    private final ValueFormatter formatter;

    public HtmlBuilder() {
        this(new ValueFormatter());
    }

    public HtmlBuilder(ValueFormatter formatter) {
        this.formatter = formatter;
        sb.append(
                """
                <!DOCTYPE html>
                <html xmlns="http://www.w3.org/1999/xhtml">
                <head>
                    <meta charset="UTF-8" />
                    <style type="text/css">
                        body { font-family: 'Liberation Sans', sans-serif; margin: 40px; }
                        h1 { font-size: 20pt; }
                        h2 { font-size: 16pt; }
                        p { font-size: 12pt; margin: 4pt 0; }
                        .italic { font-style: italic; font-size: 10pt; }
                        .indent-10 { margin-left: 10pt; }
                        .indent-20 { margin-left: 20pt; }
                        .indent-30 { margin-left: 30pt; }
                        table { width: 100%; border-collapse: collapse; margin-top: 10pt; }
                        th, td { border: 1px solid black; padding: 5px; text-align: left; }
                        td.money { text-align: right; }
                    </style>
                </head>
                <body>
                """);
    }

    public ValueFormatter getFormatter() {
        return formatter;
    }

    public HtmlBuilder append(String text) {
        sb.append(escape(text));
        return this;
    }

    private HtmlBuilder appendTag(String tag, String text) {
        sb.append("<")
                .append(tag)
                .append(">")
                .append(escape(text))
                .append("</")
                .append(tag)
                .append(">\n");
        return this;
    }

    private HtmlBuilder appendTag(String tag, String text, String clazz) {
        sb.append("<")
                .append(tag)
                .append(" class=\"")
                .append(clazz)
                .append("\">")
                .append(escape(text))
                .append("</")
                .append(tag)
                .append(">\n");
        return this;
    }

    public HtmlBuilder h1(String text) {
        return appendTag("h1", text);
    }

    public HtmlBuilder h2(String text) {
        return appendTag("h2", text);
    }

    public HtmlBuilder p(String text) {
        return appendTag("p", text);
    }

    public HtmlBuilder pItalic(String text) {
        return appendTag("p", text, "italic");
    }

    public HtmlBuilder pItalicIndented(String text, int indentPx) {
        return appendTag("p", text, "italic indent-" + indentPx);
    }

    public HtmlBuilder lineBreak() {
        sb.append("<br>");
        return this;
    }

    public HtmlBuilder beginTable(String... columnLabels) {
        sb.append("<table>\n");
        beginThead();
        beginTr();
        for (String label : columnLabels) {
            th(label);
        }
        endTr();
        endThead();
        beginTBody();
        return this;
    }

    public HtmlBuilder endTable() {
        sb.append("</table>\n");
        return this;
    }

    public HtmlBuilder beginThead() {
        sb.append("<thead>\n");
        return this;
    }

    public HtmlBuilder endThead() {
        sb.append("</thead>\n");
        return this;
    }

    public HtmlBuilder beginTBody() {
        sb.append("<tbody>\n");
        return this;
    }

    public HtmlBuilder endTBody() {
        sb.append("</tbody>\n");
        return this;
    }

    public HtmlBuilder beginTr() {
        sb.append("<tr>\n");
        return this;
    }

    public HtmlBuilder endTr() {
        sb.append("</tr>\n");
        return this;
    }

    public HtmlBuilder beginTd() {
        sb.append("<td>");
        return this;
    }

    public HtmlBuilder endTd() {
        sb.append("</td>");
        return this;
    }

    public HtmlBuilder appendDateRange(DateRange range) {
        return append(getFormatter().formatPeriod(range));
    }

    public HtmlBuilder tdText(String text) {
        return beginTd().append(text).endTd();
    }

    public HtmlBuilder tdMoney(BigDecimal amount) {
        sb.append("<td class=\"money\">").append(formatter.formatMoney(amount)).append("</td>\n");
        return this;
    }

    public HtmlBuilder th(String text) {
        sb.append("<th>").append(escape(text)).append("</th>\n");
        return this;
    }

    public HtmlBuilder tdCubicMeter(BigDecimal value) {
        sb.append("<td>")
                .append(escape(formatter.formatNumber(value)))
                .append(" m<sup>3</sup></td>\n");
        return this;
    }

    public HtmlBuilder tdCzkPerCubicMeter(BigDecimal value) {
        sb.append("<td>")
                .append(escape(formatter.formatMoney(value)))
                .append(" Kč/m<sup>3</sup></td>\n");
        return this;
    }

    public HtmlBuilder tdCzkPerMonth(BigDecimal value) {
        return tdText(formatter.formatMoney(value) + "/měsíc");
    }

    public HtmlBuilder tdCzkPerYear(BigDecimal value) {
        return tdText(formatter.formatMoney(value) + "/rok");
    }

    public HtmlBuilder tdDateRange(utilcalc.core.model.DateRange value) {
        return tdText(formatter.formatPeriod(value));
    }

    public HtmlBuilder tdMonths(BigDecimal value) {
        return tdText(formatter.formatNumber(value));
    }

    public HtmlBuilder tdYearMonth(YearMonth value) {
        return tdText(formatter.formatYearMonth(value));
    }

    public HtmlBuilder tdDateRangeWithTitle(String title, DateRange range) {
        return tdText(title + " " + formatter.formatPeriod(range));
    }

    public HtmlBuilder tdNumberWithPercent(BigDecimal number, BigDecimal percent) {
        StringBuilder content = new StringBuilder();
        if (number != null && number.compareTo(BigDecimal.ONE) != 0) {
            content.append(formatter.formatNumber(number)).append(" x ");
        }
        if (percent != null) {
            content.append(formatter.formatPercent(percent));
        }
        return tdText(content.toString());
    }

    public HtmlBuilder totalRow(int colSpan, String label, BigDecimal value) {
        beginTr();
        if (colSpan > 1) {
            sb.append("<td colspan=\"").append(colSpan).append("\">").append(label).append("</td>");
        } else {
            tdText(label);
        }
        tdMoney(value);
        endTr();
        return this;
    }

    public String build() {
        sb.append("</body></html>");
        return sb.toString();
    }

    private static String escape(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
