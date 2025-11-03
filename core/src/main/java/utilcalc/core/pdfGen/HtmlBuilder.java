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
                        h3 { font-size: 14pt; }
                        p { font-size: 12pt; margin: 4pt 0; }
                        .italic { font-style: italic; font-size: 10pt; }
                        .indent-10 { margin-left: 10pt; }
                        .indent-20 { margin-left: 20pt; }
                        .indent-30 { margin-left: 30pt; }
                        table { width: 100%; border-collapse: collapse; margin-top: 10pt; page-break-inside: avoid; break-inside: avoid; }
                        tr { page-break-inside: avoid; break-inside: avoid }
                        th, td { border: 1px solid black; padding: 5px; text-align: left; }
                        td.money { text-align: right; }
                        td.total-money { background-color: rgb(128,192,255); font-weight: bold; text-align:right; }
                        td.text-right { text-align: right; }
                        .center { text-align: center; }
                        thead { display: table-header-group; }
                        .section { page-break-inside: avoid; break-inside: avoid; margin-bottom: 20pt; }
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

    public HtmlBuilder h3(String text) {
        return appendTag("h3", text);
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

    public HtmlBuilder title(String text) {
        return beginCenter().h1(text).endCenter();
    }

    public HtmlBuilder beginCenter() {
        sb.append("<div class=\"center\">");
        return this;
    }

    public HtmlBuilder endCenter() {
        sb.append("</div>\n");
        return this;
    }

    public HtmlBuilder lineBreak() {
        sb.append("<br>");
        return this;
    }

    public HtmlBuilder beginSection(String title, int headingLevel) {
        sb.append("<div class=\"section\">");
        headingLevel = Math.max(1, Math.min(6, headingLevel));
        sb.append("<h")
                .append(headingLevel)
                .append(">")
                .append(title)
                .append("</h")
                .append(headingLevel)
                .append(">");
        return this;
    }

    public HtmlBuilder beginSection(String title) {
        return beginSection(title, 2);
    }

    public HtmlBuilder endSection() {
        sb.append("</div>\n");
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
        endTBody();
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
        return appendTag("td", text);
    }

    public HtmlBuilder tdTextRight(String text) {
        return appendTag("td", text, "text-right");
    }

    public HtmlBuilder tdMoney(BigDecimal amount) {
        return appendTag("td", formatter.formatMoney(amount), "money");
    }

    public HtmlBuilder th(String text) {
        return appendTag("th", text);
    }

    public HtmlBuilder tdCubicMeter(BigDecimal value) {
        return tdTextRight(formatter.formatNumber(value) + " m<sup>3</sup>");
    }

    public HtmlBuilder tdCzkPerCubicMeter(BigDecimal value) {
        return tdTextRight(formatter.formatMoney(value) + "/m<sup>3</sup>");
    }

    public HtmlBuilder tdCzkPerMonth(BigDecimal value) {
        return tdTextRight(formatter.formatMoney(value) + "/měsíc");
    }

    public HtmlBuilder tdCzkPerYear(BigDecimal value) {
        return tdTextRight(formatter.formatMoney(value) + "/rok");
    }

    public HtmlBuilder tdDateRange(DateRange value) {
        return tdText(formatter.formatPeriod(value));
    }

    public HtmlBuilder tdMonths(BigDecimal value) {
        return tdTextRight(formatter.formatMonths(value));
    }

    public HtmlBuilder tdYearMonth(YearMonth value) {
        return tdText(formatter.formatYearMonth(value));
    }

    public HtmlBuilder tdDateRangeWithTitle(String title, DateRange range) {
        beginTd();
        append(title);
        lineBreak();
        append(formatter.formatPeriod(range));
        endTd();
        return this;
    }

    public HtmlBuilder tdNumberWithPercent(BigDecimal number, BigDecimal percent) {
        StringBuilder content = new StringBuilder();
        if (number != null && number.compareTo(BigDecimal.ONE) != 0) {
            content.append(formatter.formatNumber(number)).append(" x ");
        }
        if (percent != null) {
            content.append(formatter.formatPercent(percent));
        }
        return tdTextRight(content.toString());
    }

    public HtmlBuilder totalRow(int colSpan, String label, BigDecimal value) {
        beginTr();
        if (colSpan > 1) {
            sb.append("<td colspan=\"")
                    .append(colSpan)
                    .append("\"><b>")
                    .append(label)
                    .append("</b></td>");
        } else {
            sb.append("<td><b>").append(label).append("</b></td>");
        }
        sb.append("<td class=\"total-money\">")
                .append(formatter.formatMoney(value))
                .append("</td>");
        endTr();
        return this;
    }

    public String build() {
        sb.append("</body></html>");
        return sb.toString();
    }

    private static String escape(String text) {
        if (text == null) return "";

        text = text.replace("<sup>", "%%SUP_START%%").replace("</sup>", "%%SUP_END%%");

        text =
                text.replace("&", "&amp;")
                        .replace("<", "&lt;")
                        .replace(">", "&gt;")
                        .replace("\"", "&quot;")
                        .replace("'", "&#39;");

        return text.replace("%%SUP_START%%", "<sup>").replace("%%SUP_END%%", "</sup>");
    }
}
