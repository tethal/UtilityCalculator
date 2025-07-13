package utilcalc.core.reportGen;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static utilcalc.core.reportGen.ColdWaterSectionGenerator.*;
import static utilcalc.core.reportGen.TestDataFactory.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import utilcalc.core.model.input.MeterReading;
import utilcalc.core.model.output.ColdWaterFee;
import utilcalc.core.model.output.ColdWaterSection;

public class ColdWaterSectionGeneratorTest {

    @Test
    void coldWaterSection_withOneWaterTariffInput_should_haveCorrectNameAndSum() {
        MeterReading meterReading1 = createMeterReading("1", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("1", "2024-06-01", "150");
        MeterReading meterReading3 = createMeterReading("1", "2025-01-01", "60");

        ColdWaterSection coldWaterSection =
                generateColdWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createColdWaterSectionInputs(
                                List.of(
                                        createWaterTariff(
                                                createDateRange("2024-01-01", "2025-01-01"), "90")),
                                List.of(meterReading1, meterReading2, meterReading3)));

        assertThat(coldWaterSection.name()).isEqualTo("Cold water");
        assertThat(coldWaterSection.priceList().size()).isEqualTo(2);
        assertThat(coldWaterSection.readings().size()).isEqualTo(2);
        assertThat(coldWaterSection.totalAmount()).isEqualTo("9900.00");
    }

    @Test
    void coldWaterSection_withOneWaterTariffInput_should_haveCorrectWaterTariffsProperties() {
        MeterReading meterReading1 = createMeterReading("1", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("1", "2024-06-01", "150");
        MeterReading meterReading3 = createMeterReading("1", "2025-01-01", "60");

        ColdWaterSection coldWaterSection =
                generateColdWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createColdWaterSectionInputs(
                                List.of(
                                        createWaterTariff(
                                                createDateRange("2024-01-01", "2025-01-01"), "90")),
                                List.of(meterReading1, meterReading2, meterReading3)));

        ColdWaterFee priceList1 = coldWaterSection.priceList().getFirst();
        ColdWaterFee priceList2 = coldWaterSection.priceList().get(1);

        assertThat(priceList1.dateRange()).isEqualTo(createDateRange("2024-01-01", "2024-06-01"));
        assertThat(priceList1.quantity()).isEqualTo("50.000");
        assertThat(priceList1.unitAmount()).isEqualTo("90");
        assertThat(priceList1.periodAmount()).isEqualTo("4500.00");

        assertThat(priceList2.dateRange()).isEqualTo(createDateRange("2024-06-01", "2025-01-01"));
        assertThat(priceList2.quantity()).isEqualTo("60.000");
        assertThat(priceList2.unitAmount()).isEqualTo("90");
        assertThat(priceList2.periodAmount()).isEqualTo("5400.00");
    }

    @Test
    void coldWaterSection_withMultipleWaterTariffInput_should_haveCorrectWaterTariffsProperties() {
        MeterReading meterReading1 = createMeterReading("1", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("1", "2024-06-01", "150");
        MeterReading meterReading3 = createMeterReading("1", "2025-01-01", "60");

        ColdWaterSection coldWaterSection =
                generateColdWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createColdWaterSectionInputs(
                                List.of(
                                        createWaterTariff(
                                                createDateRange("2024-01-01", "2024-05-01"), "90"),
                                        createWaterTariff(
                                                createDateRange("2024-05-01", "2025-01-01"),
                                                "100")),
                                List.of(meterReading1, meterReading2, meterReading3)));

        assertThat(coldWaterSection.priceList().size()).isEqualTo(3);
        assertThat(coldWaterSection.readings().size()).isEqualTo(2);
        assertThat(coldWaterSection.totalAmount()).isEqualTo("10600.00");

        ColdWaterFee priceList1 = coldWaterSection.priceList().getFirst();
        ColdWaterFee priceList2 = coldWaterSection.priceList().get(1);
        ColdWaterFee priceList3 = coldWaterSection.priceList().getLast();

        assertThat(priceList1.dateRange()).isEqualTo(createDateRange("2024-01-01", "2024-05-01"));
        assertThat(priceList1.quantity()).isEqualTo("40.000");
        assertThat(priceList1.unitAmount()).isEqualTo("90");
        assertThat(priceList1.periodAmount()).isEqualTo("3600.00");

        assertThat(priceList2.dateRange()).isEqualTo(createDateRange("2024-05-01", "2024-06-01"));
        assertThat(priceList2.quantity()).isEqualTo("10.000");
        assertThat(priceList2.unitAmount()).isEqualTo("100");
        assertThat(priceList2.periodAmount()).isEqualTo("1000.00");

        assertThat(priceList3.dateRange()).isEqualTo(createDateRange("2024-06-01", "2025-01-01"));
        assertThat(priceList3.quantity()).isEqualTo("60.000");
        assertThat(priceList3.unitAmount()).isEqualTo("100");
        assertThat(priceList3.periodAmount()).isEqualTo("6000.00");
    }

    @Test
    void coldWaterSection_withMultipleWaterMeters_should_haveCorrectWaterTariffsProperties() {
        MeterReading meterReading1 = createMeterReading("1", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("1", "2024-06-01", "150");
        MeterReading meterReading3 = createMeterReading("1", "2025-01-01", "60");
        MeterReading meterReading4 = createMeterReading("2", "2024-01-01", "100");
        MeterReading meterReading5 = createMeterReading("2", "2025-01-01", "150");

        ColdWaterSection coldWaterSection =
                generateColdWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createColdWaterSectionInputs(
                                List.of(
                                        createWaterTariff(
                                                createDateRange("2024-01-01", "2025-01-01"), "90")),
                                List.of(
                                        meterReading1,
                                        meterReading2,
                                        meterReading3,
                                        meterReading4,
                                        meterReading5)));

        ColdWaterFee priceList1 = coldWaterSection.priceList().getFirst();
        ColdWaterFee priceList2 = coldWaterSection.priceList().get(1);

        System.out.println(coldWaterSection.priceList());

        assertThat(priceList1.dateRange()).isEqualTo(createDateRange("2024-01-01", "2024-06-01"));
        assertThat(priceList1.quantity()).isEqualTo("70.833");
        assertThat(priceList1.unitAmount()).isEqualTo("90");
        assertThat(priceList1.periodAmount()).isEqualTo("6375.00");

        assertThat(priceList2.dateRange()).isEqualTo(createDateRange("2024-06-01", "2025-01-01"));
        assertThat(priceList2.quantity()).isEqualTo("89.167");
        assertThat(priceList2.unitAmount()).isEqualTo("90");
        assertThat(priceList2.periodAmount()).isEqualTo("8025.00");
    }

    @Test
    void
            coldWaterSection_withMultipleWaterMetersAndWaterTariffs_should_haveCorrectWaterTariffsProperties() {
        MeterReading meterReading1 = createMeterReading("1", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("1", "2024-06-01", "150");
        MeterReading meterReading3 = createMeterReading("1", "2025-01-01", "60");
        MeterReading meterReading4 = createMeterReading("2", "2024-01-01", "100");
        MeterReading meterReading5 = createMeterReading("2", "2025-01-01", "150");

        ColdWaterSection coldWaterSection =
                generateColdWaterSection(
                        createDateRange("2024-01-15", "2025-01-01"),
                        createColdWaterSectionInputs(
                                List.of(
                                        createWaterTariff(
                                                createDateRange("2024-01-15", "2024-12-15"), "90"),
                                        createWaterTariff(
                                                createDateRange("2024-12-15", "2025-01-01"),
                                                "100")),
                                List.of(
                                        meterReading1,
                                        meterReading2,
                                        meterReading3,
                                        meterReading4,
                                        meterReading5)));

        assertThat(coldWaterSection.priceList().size()).isEqualTo(3);
        ColdWaterFee priceList1 = coldWaterSection.priceList().getFirst();
        ColdWaterFee priceList2 = coldWaterSection.priceList().get(1);
        ColdWaterFee priceList3 = coldWaterSection.priceList().getLast();

        System.out.println(coldWaterSection.priceList());

        assertThat(priceList1.dateRange()).isEqualTo(createDateRange("2024-01-15", "2024-06-01"));
        assertThat(priceList1.quantity()).isEqualTo("64.435");
        assertThat(priceList1.unitAmount()).isEqualTo("90");
        assertThat(priceList1.periodAmount()).isEqualTo("5799.19");

        assertThat(priceList2.dateRange()).isEqualTo(createDateRange("2024-06-01", "2024-12-15"));
        assertThat(priceList2.quantity()).isEqualTo("82.181");
        assertThat(priceList2.unitAmount()).isEqualTo("90");
        assertThat(priceList2.periodAmount()).isEqualTo("7396.31");

        assertThat(priceList3.dateRange()).isEqualTo(createDateRange("2024-12-15", "2025-01-01"));
        assertThat(priceList3.quantity()).isEqualTo("6.985");
        assertThat(priceList3.unitAmount()).isEqualTo("100");
        assertThat(priceList3.periodAmount()).isEqualTo("698.54");
    }
}
