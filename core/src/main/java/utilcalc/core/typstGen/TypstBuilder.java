package utilcalc.core.typstGen;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import utilcalc.core.model.DateRange;
import utilcalc.core.utils.ValueFormatter;

@SuppressWarnings("UnusedReturnValue")
final class TypstBuilder {

	private static final String HEADER = """
			#set page(paper: "a4")
			#set text(font: "Roboto", size: 12pt)
			#let total(value) = (table.cell(fill: rgb(128,192,255), value))
			""";

	private final ValueFormatter formatter = new ValueFormatter();
	private final StringBuilder sb = new StringBuilder(HEADER);

	TypstBuilder append(String srt) {
		sb.append(srt.replaceAll("@", "\\\\@"));
		return this;
	}

	TypstBuilder appendCubicMeter(BigDecimal value) {
		return appendNumber(value).append(" m#super[3]");
	}

	TypstBuilder appendCzk(BigDecimal value) {
		return append(formatter.formatMoney(value));
	}

	TypstBuilder appendCzkPerCubicMeter(BigDecimal value) {
		return appendCzk(value).append("/m#super[3]");
	}

	TypstBuilder appendCzkPerMonth(BigDecimal value) {
		return appendCzk(value).append("/měsíc");
	}

	TypstBuilder appendCzkPerYear(BigDecimal value) {
		return appendCzk(value).append("/rok");
	}

	TypstBuilder appendDate(LocalDate date) {
		return append(formatter.formatDate(date));
	}

	TypstBuilder appendDateRange(DateRange range) {
		return append(formatter.formatPeriod(range));
	}

	TypstBuilder appendInt(int value) {
		sb.append(value);
		return this;
	}

	TypstBuilder appendMonths(BigDecimal value) {
		return append(formatter.formatMonths(value));
	}

	TypstBuilder appendNumber(BigDecimal value) {
		return append(formatter.formatNumber(value));
	}

	TypstBuilder appendPercent(BigDecimal value) {
		return append(formatter.formatPercent(value));
	}

	TypstBuilder appendYearMonth(YearMonth value) {
		return append(formatter.formatYearMonth(value));
	}

	TypstBuilder lineBreak() {
		return append(" \\\n");
	}

	TypstBuilder title(String title) {
		return append("\n#align(center, text(17pt)[*").append(title).append("*])\n");
	}

	TypstBuilder beginCenter() {
		return append("#align(center, [");
	}

	TypstBuilder endCenter() {
		return append("])\n");
	}

	TypstBuilder h1(String str) {
		return append("\n= ").append(str).append("\n");
	}

	TypstBuilder h2(String str) {
		return append("== ").append(str).append("\n");
	}

	TypstBuilder beginTable(int columns) {
		append("#table(\n");
		append("  inset: (x:5pt, y: 10pt),\n");
		append("  columns: (1fr").append(", 8.5em".repeat(columns - 1)).append("), \n");
		append("  align: (left").append(", right".repeat(columns - 1)).append("), \n");
		return this;
	}

	TypstBuilder beginTable(String... columnLabels) {
		beginTable(columnLabels.length);
		append("  table.header");
		for (String value : columnLabels) {
			sb.append("[*").append(value).append("*]");
		}
		return append(",\n");
	}

	TypstBuilder endTable() {
		return append(")\n");
	}

	TypstBuilder beginRow() {
		return append("  ");
	}

	TypstBuilder endRow() {
		return append("\n");
	}

	TypstBuilder beginCell() {
		return append("[");
	}

	TypstBuilder endCell() {
		return append("], ");
	}

	TypstBuilder totalRow(int colSpan, String label, BigDecimal value) {
		return beginRow().append("table.cell(").append("colspan: ").appendInt(colSpan).append(", [*").append(label)
				.append("*]), ").append("total([*").appendCzk(value).append("*]), ").endRow();
	}

	TypstBuilder cell(String value) {
		return beginCell().append(value).endCell();
	}

	TypstBuilder cellCubicMeter(BigDecimal value) {
		return beginCell().appendCubicMeter(value).endCell();
	}

	TypstBuilder cellCzk(BigDecimal value) {
		return beginCell().appendCzk(value).endCell();
	}

	TypstBuilder cellCzkPerCubicMeter(BigDecimal value) {
		return beginCell().appendCzkPerCubicMeter(value).endCell();
	}

	TypstBuilder cellCzkPerMonth(BigDecimal value) {
		return beginCell().appendCzkPerMonth(value).endCell();
	}

	TypstBuilder cellCzkPerYear(BigDecimal value) {
		return beginCell().appendCzkPerYear(value).endCell();
	}

	TypstBuilder cellDateRange(DateRange value) {
		return beginCell().appendDateRange(value).endCell();
	}

	TypstBuilder cellMonths(BigDecimal value) {
		return beginCell().appendMonths(value).endCell();
	}

	TypstBuilder cellYearMonth(YearMonth value) {
		return beginCell().appendYearMonth(value).endCell();
	}

	String build() {
		return sb.toString();
	}
}
