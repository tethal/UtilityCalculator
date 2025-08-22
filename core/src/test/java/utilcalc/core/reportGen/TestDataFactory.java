package utilcalc.core.reportGen;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.*;

public class TestDataFactory {

    private static final LocalDate START_DATE = LocalDate.of(2024, 1, 1);
    private static final LocalDate END_DATE = LocalDate.of(2025, 1, 1);

    private static final DateRange DATE_RANGE = new DateRange(START_DATE, END_DATE);

    private static final List<String> TENANT = List.of("Jméno nájemníka", "Adresa nemovitosti");
    private static final List<String> OWNER = List.of("Jméno majitele", "majitel@example.com");

    private static final String REPORT_PLACE = "V Praze";
    private static final LocalDate REPORT_DATE = LocalDate.of(2025, 2, 15);

    private static final List<String> EMPTY_SOURCES = List.of();
    private static final List<String> SOURCES = List.of("Vyúčtování SVJ");

    private static final List<SectionInputs> EMPTY_SECTIONS = List.of();

    public static ReportInputs validReportInputWithSections(SectionInputs... sectionInputs) {
        return new ReportInputs(
                DATE_RANGE,
                TENANT,
                OWNER,
                REPORT_PLACE,
                REPORT_DATE,
                SOURCES,
                List.of(sectionInputs));
    }

    public static ReportInputs emptySourceReportInput() {
        return new ReportInputs(
                DATE_RANGE,
                TENANT,
                OWNER,
                REPORT_PLACE,
                REPORT_DATE,
                EMPTY_SOURCES,
                EMPTY_SECTIONS);
    }

    public static Payment createPayment(String description, String count, String unitAmount) {
        return new Payment(description, new BigDecimal(count), new BigDecimal(unitAmount));
    }

    public static ServiceCost createServiceCost(DateRange dateRange, String annualCost) {
        return new ServiceCost(dateRange, new BigDecimal(annualCost));
    }

    public static DateRange createDateRange(String start, String end) {
        return new DateRange(LocalDate.parse(start), LocalDate.parse(end));
    }

    public static DepositsSectionInputs createDepositSectionInput(Payment... payments) {
        return new DepositsSectionInputs("Deposits", List.of(payments));
    }

    public static OtherFeeInputs createOtherFeeInputs(ServiceCost... serviceCosts) {
        return new OtherFeeInputs("Other fees", List.of(serviceCosts));
    }

    public static HeatingFeeInputs createHeatingFeeInputs(ServiceCost... serviceCosts) {
        return new HeatingFeeInputs("Heating fees", List.of(serviceCosts));
    }

    public static ColdWaterSectionInputs createColdWaterSectionInputs(
            List<WaterTariff> waterTariffs, List<MeterReading> meterReadings) {
        return new ColdWaterSectionInputs("Cold water", meterReadings, waterTariffs);
    }

    public static MeterReading createMeterReading(
            String meterId, String readingDate, String state) {
        return new MeterReading(meterId, LocalDate.parse(readingDate), new BigDecimal(state));
    }

    public static WaterTariff createWaterTariff(DateRange dateRange, String pricePerCubicMeter) {
        return new WaterTariff(dateRange, new BigDecimal(pricePerCubicMeter));
    }
}
