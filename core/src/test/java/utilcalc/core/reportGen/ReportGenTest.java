package utilcalc.core.reportGen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utilcalc.core.reportGen.HotWaterSectionGenerator.generateHotWaterSection;
import static utilcalc.core.reportGen.ReportGen.generateReport;
import static utilcalc.core.reportGen.TestDataFactory.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import utilcalc.core.model.input.*;
import utilcalc.core.model.output.HotWaterSection;
import utilcalc.core.model.output.Report;

class ReportGenTest {

    @Test
    void valid_ReportInput_should_return_valid_report_class() {
        ReportInputs reportInputs = validReportInputWithSections();

        Report report = generateReport(reportInputs);

        assertThat(report.dateRange()).isEqualTo(reportInputs.dateRange());
        assertThat(report.tenant()).isEqualTo(reportInputs.tenant());
        assertThat(report.owner()).isEqualTo(reportInputs.owner());
        assertThat(report.reportPlace()).isEqualTo(reportInputs.reportPlace());
        assertThat(report.reportDate()).isEqualTo(reportInputs.reportDate());
        assertThat(report.sources()).isEqualTo(reportInputs.sources());
        assertThat(report.sections()).isEmpty();
    }

    @Test
    void empty_sources_should_be_empty_in_report_class() {
        ReportInputs reportInputs = emptySourceReportInput();

        Report report = generateReport(reportInputs);

        assertThat(report.sources()).isEmpty();
    }

    @Test
    void null_ReportInputs_should_throw_IllegalArgumentException() {
        assertThatThrownBy(() -> generateReport(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Report inputs must not be null");
    }

    @Test
    void invalid_ReportInputs_should_throw_IllegalStateException() {
        InvalidSectionInput invalidSectionInput = new InvalidSectionInput(
                "Invalid section", List.of(createServiceCost(createDateRange("2024-01-01", "2025-01-01"), "8772")));

        ReportInputs reportInputs = validReportInputWithSections(invalidSectionInput);

        assertThatThrownBy(() -> generateReport(reportInputs))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unexpected section: " + invalidSectionInput.name());
    }

    @Test
    void valid_DepositSectionInputs_should_generate_valid_depositSection() {
        ReportInputs reportInputs =
                validReportInputWithSections(createDepositSectionInput(createPayment("Leden - Červen", "6", "500")));

        Report report = generateReport(reportInputs);

        assertThat(report.sections().getFirst().name()).isEqualTo("Deposits");
        assertThat(report.sections().getFirst().totalAmount()).isEqualTo("-3000");
    }

    @Test
    void valid_OtherFeeSectionInputs_should_generate_valid_otherFeeSection() {
        ReportInputs reportInputs = validReportInputWithSections(
                createOtherFeeInputs(createServiceCost(createDateRange("2024-01-01", "2025-01-01"), "8772")));

        Report report = generateReport(reportInputs);

        assertThat(report.sections().getFirst().name()).isEqualTo("Other fees");
        assertThat(report.sections().getFirst().totalAmount()).isEqualTo("8772.00");
    }

    @Test
    void valid_HeatingFeeSectionInputs_should_generate_valid_heatingFeeSection() {
        ReportInputs reportInputs = validReportInputWithSections(
                createHeatingFeeInputs(createServiceCost(createDateRange("2024-01-01", "2025-01-01"), "8772")));

        Report report = generateReport(reportInputs);

        assertThat(report.sections().getFirst().name()).isEqualTo("Heating fees");
        assertThat(report.sections().getFirst().totalAmount()).isEqualTo("8772.00");
    }

    @Test
    void valid_ColdWaterSectionInputs_should_generate_valid_coldWaterSection() {
        MeterReading meterReading1 = createMeterReading("1", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("1", "2024-06-01", "150");
        MeterReading meterReading3 = createMeterReading("1", "2024-06-01", "0");
        MeterReading meterReading4 = createMeterReading("1", "2025-01-01", "60");

        ReportInputs reportInputs = validReportInputWithSections(createColdWaterSectionInputs(
                List.of(
                        createWaterTariff(createDateRange("2024-01-01", "2024-05-01"), "90"),
                        createWaterTariff(createDateRange("2024-05-01", "2025-01-01"), "100")),
                List.of(meterReading1, meterReading2, meterReading3, meterReading4)));

        Report report = generateReport(reportInputs);

        assertThat(report.sections().getFirst().name()).isEqualTo("Cold water");
        assertThat(report.sections().getFirst().totalAmount()).isEqualTo("10600.00");
    }

    @Test
    void valid_hotWaterSection_should_generate_valid_hotWaterSection() {
        MeterReading meterReading1 = createMeterReading("kitchen", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("kitchen", "2024-06-01", "125");
        MeterReading meterReading3 = createMeterReading("kitchen", "2025-01-01", "150");

        WaterTariff waterTariff1 = createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        ServiceCost heatingBasicCost1 = createServiceCost(createDateRange("2023-02-01", "2024-02-01"), "8000");

        ServiceCost heatingBasicCost2 = createServiceCost(createDateRange("2024-02-01", "2025-02-01"), "8100");

        WaterTariff heatingConsumableTariffs1 = createWaterTariff(createDateRange("2023-02-01", "2024-02-01"), "90");

        WaterTariff heatingConsumableTariffs2 = createWaterTariff(createDateRange("2024-02-01", "2025-02-01"), "100");

        HotWaterSection hotWaterSection = generateHotWaterSection(
                createDateRange("2024-01-01", "2025-01-01"),
                createHotWaterSectionInputs(
                        List.of(meterReading1, meterReading2, meterReading3),
                        List.of(waterTariff1),
                        List.of(heatingBasicCost1, heatingBasicCost2),
                        List.of(heatingConsumableTariffs1, heatingConsumableTariffs2)));

        assertThat(hotWaterSection.name()).isEqualTo("Hot water");
        assertThat(hotWaterSection.readings().size()).isEqualTo(2);
        assertThat(hotWaterSection.priceList().size()).isEqualTo(2);
        assertThat(hotWaterSection.heatingBasicParts().size()).isEqualTo(2);
        assertThat(hotWaterSection.heatingConsumableParts().size()).isEqualTo(3);
        assertThat(hotWaterSection.totalAmount()).isEqualTo("17541.67");
    }

    private record InvalidSectionInput(String name, List<ServiceCost> serviceCosts) implements SectionInputs {}
}
