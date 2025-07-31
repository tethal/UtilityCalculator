package utilcalc.core.pdfGen;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import utilcalc.core.model.output.ReportSection;

public final class HtmlBuilder {

    private final StringBuilder sb = new StringBuilder();

    public HtmlBuilder() {
        sb.append(
                """
            <!DOCTYPE html>
            <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
                <meta charset="UTF-8" />
                <style type="text/css">
                    body { font-family: 'Liberation Sans', sans-serif; margin: 40px; }
                    h1 { font-size: 20pt; }
                    p { font-size: 12pt; margin: 4pt 0; }
                    .italic { font-style: italic; font-size: 10pt; }
                    .indent-10 { margin-left: 10pt; }
                    .indent-20 { margin-left: 20pt; }
                    .indent-30 { margin-left: 30pt; }
                </style>
            </head>
            <body>
        """);
    }

    public HtmlBuilder h1(String text) {
        sb.append("<h1>").append(escape(text)).append("</h1>\n");
        return this;
    }

    public HtmlBuilder p(String text) {
        sb.append("<p>").append(escape(text)).append("</p>\n");
        return this;
    }

    public HtmlBuilder pItalic(String text) {
        sb.append("<p class=\"italic\">").append(escape(text)).append("</p>\n");
        return this;
    }

    public HtmlBuilder pItalicIndented(String text, int indentPx) {
        sb.append("<p class=\"italic indent-")
                .append(indentPx)
                .append("pt;\">")
                .append(escape(text))
                .append("</p>\n");
        return this;
    }

    public String build() {
        sb.append("</body></html>");
        return sb.toString();
    }

    private static String escape(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    public HtmlBuilder renderSummaryTable(List<ReportSection> sections) {
        sb.append("<h1>Celkový přehled</h1>");
        sb.append("<table style=\"border-collapse: collapse; width: 100%;\">");
        sb.append("<thead><tr>");
        sb.append(
                "<th style=\"border: 1px solid black; padding: 5px; text-align: left;\">Popis</th>");
        sb.append(
                "<th style=\"border: 1px solid black; padding: 5px; text-align: right;\">Částka</th>");
        sb.append("</tr></thead>");
        sb.append("<tbody>");

        for (ReportSection section : sections) {
            sb.append("<tr>");
            sb.append("<td style=\"border: 1px solid black; padding: 5px;\">")
                    .append(escape(section.name()))
                    .append("</td>");
            sb.append("<td style=\"border: 1px solid black; padding: 5px; text-align: right;\">")
                    .append(formatMoney(section.totalAmount()))
                    .append("</td>");
            sb.append("</tr>");
        }

        sb.append("</tbody></table>");
        return this;
    }

    private String formatMoney(BigDecimal amount) {
        if (amount == null) return "";
        return amount.setScale(2, RoundingMode.HALF_UP).toPlainString() + " Kč";
    }
}
