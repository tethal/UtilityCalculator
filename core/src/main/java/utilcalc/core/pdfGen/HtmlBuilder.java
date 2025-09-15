package utilcalc.core.pdfGen;

import java.math.BigDecimal;

public final class HtmlBuilder {

    private final StringBuilder sb = new StringBuilder();
    private final ReportFormatter formatter;

    public HtmlBuilder() {
        this(new ReportFormatter());
    }

    public HtmlBuilder(ReportFormatter formatter) {
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

    public ReportFormatter getFormatter() {
        return this.formatter;
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

    public HtmlBuilder beginTable() {
        sb.append("<table>\n");
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

    public HtmlBuilder td(String text) {
        sb.append("<td>").append(escape(text)).append("</td>\n");
        return this;
    }

    public HtmlBuilder tdNumber(BigDecimal number) {
        sb.append("<td class=\"money\">")
                .append(escape(formatter.formatNumber(number)))
                .append("</td>\n");
        return this;
    }

    public HtmlBuilder tdMoney(BigDecimal amount) {
        sb.append("<td class=\"money\">")
                .append(escape(formatter.formatMoney(amount)))
                .append("</td>\n");
        return this;
    }

    public HtmlBuilder th(String text) {
        sb.append("<th>").append(escape(text)).append("</th>\n");
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
}
