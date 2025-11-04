package utilcalc.core.typstGen;

import java.math.BigDecimal;
import java.util.List;
import utilcalc.core.model.output.*;

public final class TypstGenerator {

	private TypstGenerator() {
	}

	public static String generateTypst(Report report) {
		TypstBuilder b = new TypstBuilder();
		b.title("Vyúčtování poplatků za služby a energie");
		b.beginCenter().append("Období: ").appendDateRange(report.dateRange()).endCenter();
		report.tenant().forEach(t -> b.append(t).lineBreak());
		generateSummary(b, report);
		report.sections().forEach(section -> generateSection(section, b));
		b.append(report.reportPlace()).append(", ").appendDate(report.reportDate()).lineBreak().lineBreak();
		report.owner().forEach(o -> b.append(o).lineBreak());
		return b.build();
	}

	private static void generateSummary(TypstBuilder b, Report report) {
		b.h1("Celkový přehled");
		b.beginTable(2);
		report.sections().forEach(s -> b.beginRow().cell(s.name()).cellCzk(s.totalAmount()).endRow());
		b.totalRow(1, report.total().signum() == -1 ? "Přeplatek" : "Nedoplatek", report.total().abs());
		b.endTable();
		if (!report.sources().isEmpty()) {
			b.append("Zdroje:").lineBreak();
			report.sources().forEach(s -> b.append(s).lineBreak());
		}
	}

	private static void generateSection(ReportSection section, TypstBuilder b) {
		b.h1(section.name());
		switch (section) {
			case ColdWaterSection s -> generateColdWater(b, s);
			case HotWaterSection s -> generateHotWater(b, s);
			case HeatingFeeSection s -> generateHeating(b, s);
			case OtherFeeSection s -> generateOtherFee(b, s);
			case DepositSection s -> generateDeposit(b, s);
			default -> throw new IllegalStateException("Unexpected value: " + section);
		}
	}

	private static void waterReadings(TypstBuilder b, List<WaterReading> readings) {
		boolean showMeterId = readings.stream().map(WaterReading::meterId).distinct().count() > 1;
		b.h2("Odečty");
		b.beginTable("Období", "Počáteční stav", "Konečný stav", "Spotřeba");
		readings.forEach(r -> {
			b.beginRow();
			b.beginCell().appendDateRange(r.dateRange());
			if (showMeterId) {
				b.lineBreak().append(r.meterId());
			}
			b.endCell();
			b.cellCubicMeter(r.startState());
			b.cellCubicMeter(r.endState());
			b.cellCubicMeter(r.consumption());
			b.endRow();
		});
		b.endTable();
	}

	private static void generateColdWater(TypstBuilder b, ColdWaterSection section) {
		waterReadings(b, section.readings());
		b.h2("Náklady");
		b.beginTable("Období", "Množství", "Sazba", "Cena");
		section.priceList().forEach(i -> {
			b.beginRow();
			b.cellDateRange(i.dateRange());
			b.cellCubicMeter(i.quantity());
			b.cellCzkPerCubicMeter(i.unitAmount());
			b.cellCzk(i.periodAmount());
			b.endRow();
		});
		b.totalRow(3, "CELKEM", section.totalAmount());
		b.endTable();
	}

	private static void generateHotWater(TypstBuilder b, HotWaterSection section) {
		waterReadings(b, section.readings());
		b.h2("Náklady");
		b.beginTable("Popis", "Množství", "Sazba", "Cena");
		section.priceList().forEach(i -> {
			b.beginRow();
			b.beginCell().append("Studená voda").lineBreak().appendDateRange(i.dateRange()).endCell();
			b.cellCubicMeter(i.quantity());
			b.cellCzkPerCubicMeter(i.unitAmount());
			b.cellCzk(i.periodAmount());
			b.endRow();
		});
		section.heatingBasicParts().forEach(i -> {
			b.beginRow();
			b.beginCell().append("Ohřev základní složka").lineBreak().appendDateRange(i.dateRange()).endCell();
			b.cellMonths(i.numberOfMonths());
			b.cellCzkPerMonth(i.monthlyCost());
			b.cellCzk(i.totalAmount());
			b.endRow();
		});
		section.heatingConsumableParts().forEach(i -> {
			b.beginRow();
			b.beginCell().append("Ohřev spotřební složka").lineBreak().appendDateRange(i.dateRange()).endCell();
			b.cellCubicMeter(i.unitAmount());
			b.cellCzkPerCubicMeter(i.unitCost());
			b.cellCzk(i.totalCost());
			b.endRow();
		});
		b.totalRow(3, "CELKEM", section.totalAmount());
		b.endTable();
	}

	private static void generateHeating(TypstBuilder b, HeatingFeeSection section) {
		b.beginTable("Období", "Koeficient", "Sazba", "Cena");
		section.fees().forEach(i -> {
			b.beginRow();
			b.cellYearMonth(i.yearMonth());
			b.beginCell();
			if (i.monthCount().compareTo(BigDecimal.ONE) != 0) {
				b.appendNumber(i.monthCount()).append(" x ");
			}
			b.appendPercent(i.coefficient()).endCell();
			b.cellCzkPerYear(i.annualCost());
			b.cellCzk(i.feeAmount());
			b.endRow();
		});
		b.totalRow(3, "CELKEM", section.totalAmount());
		b.endTable();
	}

	private static void generateOtherFee(TypstBuilder b, OtherFeeSection section) {
		b.beginTable("Období", "Množství", "Sazba", "Cena");
		section.fees().forEach(i -> {
			b.beginRow();
			b.cellDateRange(i.dateRange());
			b.cellMonths(i.monthCount());
			b.cellCzkPerMonth(i.monthlyCost());
			b.cellCzk(i.feeAmount());
			b.endRow();
		});
		b.totalRow(3, "CELKEM", section.totalAmount());
		b.endTable();
	}

	private static void generateDeposit(TypstBuilder b, DepositSection section) {
		boolean extended = section.deposits().stream().anyMatch(d -> d.count().compareTo(BigDecimal.ONE) != 0);
		if (extended) {
			b.beginTable("Popis", "Množství", "Záloha", "Částka");
		} else {
			b.beginTable("Popis", "Částka");
		}
		section.deposits().forEach(i -> {
			b.beginRow();
			b.cell(i.description());
			if (extended) {
				b.cellMonths(i.count());
				b.cellCzkPerMonth(i.unitAmount());
			}
			b.cellCzk(i.amount());
			b.endRow();
		});
		b.totalRow(extended ? 3 : 1, "CELKEM", section.totalAmount().abs());
		b.endTable();
	}
}
