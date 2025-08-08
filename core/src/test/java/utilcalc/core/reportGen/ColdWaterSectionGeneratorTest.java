package utilcalc.core.reportGen;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static utilcalc.core.reportGen.ColdWaterSectionGenerator.*;
import static utilcalc.core.reportGen.TestDataFactory.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import utilcalc.core.model.input.MeterReading;
import utilcalc.core.model.input.WaterTariff;
import utilcalc.core.model.output.ColdWaterFee;
import utilcalc.core.model.output.ColdWaterSection;
import utilcalc.core.model.output.WaterReading;

public class ColdWaterSectionGeneratorTest {

    // region Cold water reading tests
    @Test
    void coldWaterSection_withOneMeterId_should_haveCorrectColdWaterReading() {
        MeterReading meterReading1 = createMeterReading("kitchen", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("kitchen", "2024-06-01", "125");
        MeterReading meterReading3 = createMeterReading("kitchen", "2025-01-01", "150");

        WaterTariff waterTariff =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        ColdWaterSection coldWaterSection =
                generateColdWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createColdWaterSectionInputs(
                                List.of(waterTariff),
                                List.of(meterReading1, meterReading2, meterReading3)));

        assertThat(coldWaterSection.name()).isEqualTo("Cold water");
        assertThat(coldWaterSection.readings().size()).isEqualTo(2);
        assertThat(coldWaterSection.priceList().size()).isEqualTo(2);
        assertThat(coldWaterSection.totalAmount()).isEqualTo("4500.00");

        WaterReading waterReading1 = coldWaterSection.readings().getFirst();
        assertThat(waterReading1.meterId()).isEqualTo("kitchen");
        assertThat(waterReading1.dateRange())
                .isEqualTo(createDateRange("2024-01-01", "2024-06-01"));
        assertThat(waterReading1.startState()).isEqualTo("100");
        assertThat(waterReading1.endState()).isEqualTo("125");
        assertThat(waterReading1.consumption()).isEqualTo("25");

        WaterReading waterReading2 = coldWaterSection.readings().getLast();
        assertThat(waterReading2.meterId()).isEqualTo("kitchen");
        assertThat(waterReading2.dateRange())
                .isEqualTo(createDateRange("2024-06-01", "2025-01-01"));
        assertThat(waterReading2.startState()).isEqualTo("125");
        assertThat(waterReading2.endState()).isEqualTo("150");
        assertThat(waterReading2.consumption()).isEqualTo("25");
    }

    @Test
    void coldWaterSection_withOneMeterId_withMissingReadings_should_haveCorrectColdWaterReading() {
        MeterReading meterReading1 = createMeterReading("kitchen", "2024-02-01", "100");
        MeterReading meterReading2 = createMeterReading("kitchen", "2024-06-01", "125");
        MeterReading meterReading3 = createMeterReading("kitchen", "2024-09-01", "150");

        WaterTariff waterTariff =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        ColdWaterSection coldWaterSection =
                generateColdWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createColdWaterSectionInputs(
                                List.of(waterTariff),
                                List.of(meterReading1, meterReading2, meterReading3)));

        assertThat(coldWaterSection.name()).isEqualTo("Cold water");
        assertThat(coldWaterSection.readings().size()).isEqualTo(4);
        assertThat(coldWaterSection.priceList().size()).isEqualTo(4);
        assertThat(coldWaterSection.totalAmount()).isEqualTo("8062.47");

        WaterReading waterReading1 = coldWaterSection.readings().getFirst();
        assertThat(waterReading1.meterId()).isEqualTo("kitchen");
        assertThat(waterReading1.dateRange())
                .isEqualTo(createDateRange("2024-01-01", "2024-02-01"));
        assertThat(waterReading1.startState()).isEqualTo("93.750");
        assertThat(waterReading1.endState()).isEqualTo("100");
        assertThat(waterReading1.consumption()).isEqualTo("6.250");

        WaterReading waterReading2 = coldWaterSection.readings().get(1);
        assertThat(waterReading2.meterId()).isEqualTo("kitchen");
        assertThat(waterReading2.dateRange())
                .isEqualTo(createDateRange("2024-02-01", "2024-06-01"));
        assertThat(waterReading2.startState()).isEqualTo("100");
        assertThat(waterReading2.endState()).isEqualTo("125");
        assertThat(waterReading2.consumption()).isEqualTo("25");

        WaterReading waterReading3 = coldWaterSection.readings().get(2);
        assertThat(waterReading3.meterId()).isEqualTo("kitchen");
        assertThat(waterReading3.dateRange())
                .isEqualTo(createDateRange("2024-06-01", "2024-09-01"));
        assertThat(waterReading3.startState()).isEqualTo("125");
        assertThat(waterReading3.endState()).isEqualTo("150");
        assertThat(waterReading3.consumption()).isEqualTo("25");

        WaterReading waterReading4 = coldWaterSection.readings().getLast();
        assertThat(waterReading4.meterId()).isEqualTo("kitchen");
        assertThat(waterReading4.dateRange())
                .isEqualTo(createDateRange("2024-09-01", "2025-01-01"));
        assertThat(waterReading4.startState()).isEqualTo("150");
        assertThat(waterReading4.endState()).isEqualTo("183.333");
        assertThat(waterReading4.consumption()).isEqualTo("33.333");
    }

    @Test
    void coldWaterSection_withReplaceMeter_should_haveCorrectColdWaterReading() {
        MeterReading meterReading1 = createMeterReading("kitchen", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("kitchen", "2024-06-01", "125");
        MeterReading meterReading3 = createMeterReading("kitchen", "2024-06-01", "0");
        MeterReading meterReading4 = createMeterReading("kitchen", "2025-01-01", "30");

        WaterTariff waterTariff =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        ColdWaterSection coldWaterSection =
                generateColdWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createColdWaterSectionInputs(
                                List.of(waterTariff),
                                List.of(
                                        meterReading1,
                                        meterReading2,
                                        meterReading3,
                                        meterReading4)));

        assertThat(coldWaterSection.name()).isEqualTo("Cold water");
        assertThat(coldWaterSection.readings().size()).isEqualTo(2);
        assertThat(coldWaterSection.priceList().size()).isEqualTo(2);
        assertThat(coldWaterSection.totalAmount()).isEqualTo("4950.00");

        WaterReading waterReading1 = coldWaterSection.readings().getFirst();
        assertThat(waterReading1.meterId()).isEqualTo("kitchen");
        assertThat(waterReading1.dateRange())
                .isEqualTo(createDateRange("2024-01-01", "2024-06-01"));
        assertThat(waterReading1.startState()).isEqualTo("100");
        assertThat(waterReading1.endState()).isEqualTo("125");
        assertThat(waterReading1.consumption()).isEqualTo("25");

        WaterReading waterReading2 = coldWaterSection.readings().getLast();
        assertThat(waterReading2.meterId()).isEqualTo("kitchen");
        assertThat(waterReading2.dateRange())
                .isEqualTo(createDateRange("2024-06-01", "2025-01-01"));
        assertThat(waterReading2.startState()).isEqualTo("0");
        assertThat(waterReading2.endState()).isEqualTo("30");
        assertThat(waterReading2.consumption()).isEqualTo("30");
    }

    @Test
    void
            coldWaterSection_withMultipleMeters_withMisingReadings_should_haveCorrectColdWaterReading() {
        MeterReading meterReading1 = createMeterReading("kitchen", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("kitchen", "2024-06-01", "125");
        MeterReading meterReading3 = createMeterReading("bathroom", "2024-01-01", "0");
        MeterReading meterReading4 = createMeterReading("bathroom", "2024-06-01", "30");

        WaterTariff waterTariff =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        ColdWaterSection coldWaterSection =
                generateColdWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createColdWaterSectionInputs(
                                List.of(waterTariff),
                                List.of(
                                        meterReading1,
                                        meterReading2,
                                        meterReading3,
                                        meterReading4)));

        assertThat(coldWaterSection.name()).isEqualTo("Cold water");
        assertThat(coldWaterSection.readings().size()).isEqualTo(4);
        assertThat(coldWaterSection.priceList().size()).isEqualTo(2);
        assertThat(coldWaterSection.totalAmount()).isEqualTo("11880.00");

        WaterReading waterReading1 = coldWaterSection.readings().getFirst();
        assertThat(waterReading1.meterId()).isEqualTo("kitchen");
        assertThat(waterReading1.dateRange())
                .isEqualTo(createDateRange("2024-01-01", "2024-06-01"));
        assertThat(waterReading1.startState()).isEqualTo("100");
        assertThat(waterReading1.endState()).isEqualTo("125");
        assertThat(waterReading1.consumption()).isEqualTo("25");

        WaterReading waterReading2 = coldWaterSection.readings().get(1);
        assertThat(waterReading2.meterId()).isEqualTo("kitchen");
        assertThat(waterReading2.dateRange())
                .isEqualTo(createDateRange("2024-06-01", "2025-01-01"));
        assertThat(waterReading2.startState()).isEqualTo("125");
        assertThat(waterReading2.endState()).isEqualTo("160.000");
        assertThat(waterReading2.consumption()).isEqualTo("35.000");

        WaterReading waterReading3 = coldWaterSection.readings().get(2);
        assertThat(waterReading3.meterId()).isEqualTo("bathroom");
        assertThat(waterReading3.dateRange())
                .isEqualTo(createDateRange("2024-01-01", "2024-06-01"));
        assertThat(waterReading3.startState()).isEqualTo("0");
        assertThat(waterReading3.endState()).isEqualTo("30");
        assertThat(waterReading3.consumption()).isEqualTo("30");

        WaterReading waterReading4 = coldWaterSection.readings().getLast();
        assertThat(waterReading4.meterId()).isEqualTo("bathroom");
        assertThat(waterReading4.dateRange())
                .isEqualTo(createDateRange("2024-06-01", "2025-01-01"));
        assertThat(waterReading4.startState()).isEqualTo("30");
        assertThat(waterReading4.endState()).isEqualTo("72.000");
        assertThat(waterReading4.consumption()).isEqualTo("42.000");
    }

    // endregion
    // region Cold water fee tests
    @Test
    void coldWaterSection_withOneMeterId_withMultipleTariff_should_haveCorrectColdWaterReading() {
        MeterReading meterReading1 = createMeterReading("kitchen", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("kitchen", "2024-06-01", "125");
        MeterReading meterReading3 = createMeterReading("kitchen", "2025-01-01", "150");

        WaterTariff waterTariff1 =
                createWaterTariff(createDateRange("2024-01-01", "2024-06-01"), "90");
        WaterTariff waterTariff2 =
                createWaterTariff(createDateRange("2024-06-01", "2025-01-01"), "100");

        ColdWaterSection coldWaterSection =
                generateColdWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createColdWaterSectionInputs(
                                List.of(waterTariff2, waterTariff1),
                                List.of(meterReading2, meterReading1, meterReading3)));

        assertThat(coldWaterSection.name()).isEqualTo("Cold water");
        assertThat(coldWaterSection.readings().size()).isEqualTo(2);
        assertThat(coldWaterSection.priceList().size()).isEqualTo(2);
        assertThat(coldWaterSection.totalAmount()).isEqualTo("4750.00");

        ColdWaterFee coldWaterFee1 = coldWaterSection.priceList().getFirst();
        assertThat(coldWaterFee1.dateRange())
                .isEqualTo(createDateRange("2024-01-01", "2024-06-01"));
        assertThat(coldWaterFee1.unitAmount()).isEqualTo("90");
        assertThat(coldWaterFee1.quantity()).isEqualTo("25.000");
        assertThat(coldWaterFee1.periodAmount()).isEqualTo("2250.00");

        ColdWaterFee coldWaterFee2 = coldWaterSection.priceList().getLast();
        assertThat(coldWaterFee2.dateRange())
                .isEqualTo(createDateRange("2024-06-01", "2025-01-01"));
        assertThat(coldWaterFee2.unitAmount()).isEqualTo("100");
        assertThat(coldWaterFee2.quantity()).isEqualTo("25.000");
        assertThat(coldWaterFee2.periodAmount()).isEqualTo("2500.00");
    }

    @Test
    void
            coldWaterSection_withMultipleMeters_withMisingReadings_withMultipleTariff_should_haveCorrectColdWaterReading() {
        MeterReading meterReading1 = createMeterReading("kitchen", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("kitchen", "2024-06-01", "125");
        MeterReading meterReading3 = createMeterReading("bathroom", "2024-01-01", "0");
        MeterReading meterReading4 = createMeterReading("bathroom", "2024-06-01", "30");

        WaterTariff waterTariff1 =
                createWaterTariff(createDateRange("2024-01-01", "2024-06-01"), "90");
        WaterTariff waterTariff2 =
                createWaterTariff(createDateRange("2024-06-01", "2025-01-01"), "100");

        ColdWaterSection coldWaterSection =
                generateColdWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createColdWaterSectionInputs(
                                List.of(waterTariff1, waterTariff2),
                                List.of(
                                        meterReading1,
                                        meterReading2,
                                        meterReading3,
                                        meterReading4)));

        assertThat(coldWaterSection.name()).isEqualTo("Cold water");
        assertThat(coldWaterSection.readings().size()).isEqualTo(4);
        assertThat(coldWaterSection.priceList().size()).isEqualTo(2);
        assertThat(coldWaterSection.totalAmount()).isEqualTo("12650.00");

        ColdWaterFee coldWaterFee1 = coldWaterSection.priceList().getFirst();
        assertThat(coldWaterFee1.dateRange())
                .isEqualTo(createDateRange("2024-01-01", "2024-06-01"));
        assertThat(coldWaterFee1.unitAmount()).isEqualTo("90");
        assertThat(coldWaterFee1.quantity()).isEqualTo("55.000");
        assertThat(coldWaterFee1.periodAmount()).isEqualTo("4950.00");

        ColdWaterFee coldWaterFee2 = coldWaterSection.priceList().getLast();
        assertThat(coldWaterFee2.dateRange())
                .isEqualTo(createDateRange("2024-06-01", "2025-01-01"));
        assertThat(coldWaterFee2.unitAmount()).isEqualTo("100");
        assertThat(coldWaterFee2.quantity()).isEqualTo("77.000");
        assertThat(coldWaterFee2.periodAmount()).isEqualTo("7700.00");
    }

    @Test
    void
            coldWaterSection_withOneMeterId_withMultipleTariff_withMisingTariffZone_should_haveCorrectColdWaterReading() {
        MeterReading meterReading1 = createMeterReading("kitchen", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("kitchen", "2024-06-01", "125");
        MeterReading meterReading3 = createMeterReading("kitchen", "2025-01-01", "150");

        WaterTariff waterTariff1 =
                createWaterTariff(createDateRange("2024-01-01", "2024-04-01"), "90");
        WaterTariff waterTariff2 =
                createWaterTariff(createDateRange("2024-04-01", "2025-01-01"), "100");

        ColdWaterSection coldWaterSection =
                generateColdWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createColdWaterSectionInputs(
                                List.of(waterTariff1, waterTariff2),
                                List.of(meterReading1, meterReading2, meterReading3)));

        assertThat(coldWaterSection.name()).isEqualTo("Cold water");
        assertThat(coldWaterSection.readings().size()).isEqualTo(2);
        assertThat(coldWaterSection.priceList().size()).isEqualTo(3);
        assertThat(coldWaterSection.totalAmount()).isEqualTo("4850.00");

        ColdWaterFee coldWaterFee1 = coldWaterSection.priceList().getFirst();
        assertThat(coldWaterFee1.dateRange())
                .isEqualTo(createDateRange("2024-01-01", "2024-04-01"));
        assertThat(coldWaterFee1.unitAmount()).isEqualTo("90");
        assertThat(coldWaterFee1.quantity()).isEqualTo("15.000");
        assertThat(coldWaterFee1.periodAmount()).isEqualTo("1350.00");

        ColdWaterFee coldWaterFee2 = coldWaterSection.priceList().get(1);
        assertThat(coldWaterFee2.dateRange())
                .isEqualTo(createDateRange("2024-04-01", "2024-06-01"));
        assertThat(coldWaterFee2.unitAmount()).isEqualTo("100");
        assertThat(coldWaterFee2.quantity()).isEqualTo("10.000");
        assertThat(coldWaterFee2.periodAmount()).isEqualTo("1000.00");

        ColdWaterFee coldWaterFee3 = coldWaterSection.priceList().getLast();
        assertThat(coldWaterFee3.dateRange())
                .isEqualTo(createDateRange("2024-06-01", "2025-01-01"));
        assertThat(coldWaterFee3.unitAmount()).isEqualTo("100");
        assertThat(coldWaterFee3.quantity()).isEqualTo("25.000");
        assertThat(coldWaterFee3.periodAmount()).isEqualTo("2500.00");
    }
    // endregion
}
