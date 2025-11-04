package utilcalc.core.integration;

import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.output.*;
import utilcalc.core.parser.Parser;
import utilcalc.core.reportGen.ReportGen;
import utilcalc.core.util.TestHelpers;

class RealDataIntegrationTest {

    @ValueSource(strings = {"2017", "2018", "2019", "2020", "2021", "2023", "2024", "2024_simple"})
    @ParameterizedTest
    void testGoldenFiles(String name) {
        TestHelpers.goldenTest(
                "real/" + name,
                src ->
                        new Dumper()
                                .dumpReport(ReportGen.generateReport(Parser.parse(src)))
                                .toString());
    }

    private static final class Dumper {
        private final StringBuilder sb = new StringBuilder();

        @Override
        public String toString() {
            return sb.toString();
        }

        Dumper dumpReport(Report report) {
            append("Report:\n");
            append("  dateRange: ").append(report.dateRange()).append("\n");
            dumpStringList("tenant", report.tenant());
            dumpStringList("owner", report.owner());
            dumpStringList(
                    "placeDate", List.of(report.reportPlace(), report.reportDate().toString()));
            dumpStringList("sources", report.sources());
            append("  sections:\n");
            dumpTable(
                    report.sections(),
                    "    %: %\n",
                    ReportSection::name,
                    ReportSection::totalAmount);
            append("    TOTAL: ").append(report.total()).append("\n");
            report.sections().forEach(this::dumpSection);
            return this;
        }

        private void dumpSection(ReportSection section) {
            append("\n").append(section.name()).append(":").append("\n");
            switch (section) {
                case DepositSection s -> dumpDepositSection(s);
                case ColdWaterSection s -> dumpColdWaterSection(s);
                case HotWaterSection s -> dumpHotWaterSection(s);
                case HeatingFeeSection s -> dumpHeatingFeeSection(s);
                case OtherFeeSection s -> dumpOtherFeeSection(s);
                default ->
                        throw new IllegalStateException(
                                "Unexpected section: " + section.getClass());
            }
        }

        private void dumpDepositSection(DepositSection section) {
            dumpTable(
                    section.deposits(),
                    "  %: % * % = %\n",
                    Deposit::description,
                    Deposit::count,
                    Deposit::unitAmount,
                    Deposit::amount);
        }

        private void dumpColdWaterSection(ColdWaterSection section) {
            dumpWaterReadings(section.readings());
            dumpWaterFees(section.priceList());
        }

        private void dumpHotWaterSection(HotWaterSection section) {
            dumpWaterReadings(section.readings());
            dumpWaterFees(section.priceList());
            append("  heatingBasicParts:\n");
            dumpTable(
                    section.heatingBasicParts(),
                    "    %: % * % = %\n",
                    WaterHeatingBasicPart::dateRange,
                    WaterHeatingBasicPart::numberOfMonths,
                    WaterHeatingBasicPart::monthlyCost,
                    WaterHeatingBasicPart::totalAmount);

            append("  heatingConsumableParts:\n");
            dumpTable(
                    section.heatingConsumableParts(),
                    "    %: % * % = %\n",
                    WaterHeatingConsumablePart::dateRange,
                    WaterHeatingConsumablePart::unitAmount,
                    WaterHeatingConsumablePart::unitCost,
                    WaterHeatingConsumablePart::totalCost);
        }

        private void dumpHeatingFeeSection(HeatingFeeSection section) {
            dumpTable(
                    section.fees(),
                    "  %: % * % * % = %\n",
                    HeatingFee::yearMonth,
                    HeatingFee::monthCount,
                    HeatingFee::annualCost,
                    HeatingFee::coefficient,
                    HeatingFee::feeAmount);
        }

        private void dumpOtherFeeSection(OtherFeeSection section) {
            dumpTable(
                    section.fees(),
                    "  %: % * % = %\n",
                    OtherFee::dateRange,
                    OtherFee::monthCount,
                    OtherFee::monthlyCost,
                    OtherFee::feeAmount);
        }

        private void dumpWaterReadings(List<WaterReading> readings) {
            append("  readings:\n");
            dumpTable(
                    readings,
                    "    [%] %: %...% = %\n",
                    WaterReading::meterId,
                    WaterReading::dateRange,
                    WaterReading::startState,
                    WaterReading::endState,
                    WaterReading::consumption);
        }

        private void dumpWaterFees(List<WaterFee> waterFees) {
            append("  priceList:\n");
            dumpTable(
                    waterFees,
                    "    %: % * % = %\n",
                    WaterFee::dateRange,
                    WaterFee::quantity,
                    WaterFee::unitAmount,
                    WaterFee::periodAmount);
        }

        @SafeVarargs
        private <T> void dumpTable(
                List<T> items, String format, Function<T, ?>... columnExtractor) {
            int columns = columnExtractor.length;
            String[] separators = format.split("%");
            assert separators.length == columns + 1;
            items.forEach(
                    item -> {
                        for (int i = 0; i < columns; i++) {
                            append(separators[i]).append(columnExtractor[i].apply(item));
                        }
                        append(separators[columns]);
                    });
        }

        private void dumpStringList(String label, List<String> stringList) {
            append("  ")
                    .append(label)
                    .append(": ")
                    .append(String.join(", ", stringList))
                    .append("\n");
        }

        private Dumper append(Object value) {
            if (value instanceof DateRange dateRange) {
                append(dateRange.startDate()).append("...").append(dateRange.endDateExclusive());
            } else {
                sb.append(value);
            }
            return this;
        }
    }
}
