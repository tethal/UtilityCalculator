package utilcalc.core.reportGen;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static utilcalc.core.reportGen.HotWaterSectionGenerator.generateHotWaterSection;
import static utilcalc.core.reportGen.TestDataFactory.*;
import static utilcalc.core.reportGen.TestDataFactory.createDateRange;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import utilcalc.core.model.input.MeterReading;
import utilcalc.core.model.input.ServiceCost;
import utilcalc.core.model.input.WaterTariff;
import utilcalc.core.model.output.*;

public class HotWaterSectionGeneratorTest {

    // region reading tests
    @Test
    void hotWaterSection_withOneMeterId_should_haveCorrectWaterReading() {
        MeterReading meterReading1 = createMeterReading("kitchen", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("kitchen", "2024-06-01", "125");
        MeterReading meterReading3 = createMeterReading("kitchen", "2025-01-01", "150");

        WaterTariff waterTariff =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        ServiceCost heatingBasicCost =
                createServiceCost(createDateRange("2024-01-01", "2025-01-01"), "8000");

        WaterTariff heatingConsumableTariffs =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        HotWaterSection hotWaterSection =
                generateHotWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createHotWaterSectionInputs(
                                List.of(meterReading1, meterReading2, meterReading3),
                                List.of(waterTariff),
                                List.of(heatingBasicCost),
                                List.of(heatingConsumableTariffs)));

        assertThat(hotWaterSection.name()).isEqualTo("Hot water");
        assertThat(hotWaterSection.readings().size()).isEqualTo(2);
        Assertions.assertThat(hotWaterSection.totalAmount()).isEqualTo("17000.00");

        WaterReading waterReading1 = hotWaterSection.readings().getFirst();
        assertThat(waterReading1.meterId()).isEqualTo("kitchen");
        assertThat(waterReading1.dateRange())
                .isEqualTo(createDateRange("2024-01-01", "2024-06-01"));
        assertThat(waterReading1.startState()).isEqualTo("100");
        assertThat(waterReading1.endState()).isEqualTo("125");
        assertThat(waterReading1.consumption()).isEqualTo("25");

        WaterReading waterReading2 = hotWaterSection.readings().getLast();
        assertThat(waterReading2.meterId()).isEqualTo("kitchen");
        assertThat(waterReading2.dateRange())
                .isEqualTo(createDateRange("2024-06-01", "2025-01-01"));
        assertThat(waterReading2.startState()).isEqualTo("125");
        assertThat(waterReading2.endState()).isEqualTo("150");
        assertThat(waterReading2.consumption()).isEqualTo("25");
    }

    @Test
    void hotWaterSection_withOneMeterId_withMissingReadings_should_haveCorrectWaterReading() {
        MeterReading meterReading1 = createMeterReading("kitchen", "2024-02-01", "100");
        MeterReading meterReading2 = createMeterReading("kitchen", "2024-06-01", "125");
        MeterReading meterReading3 = createMeterReading("kitchen", "2024-09-01", "150");

        WaterTariff waterTariff =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        ServiceCost heatingBasicCost =
                createServiceCost(createDateRange("2024-01-01", "2025-01-01"), "8000");

        WaterTariff heatingConsumableTariffs =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        HotWaterSection hotWaterSection =
                generateHotWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createHotWaterSectionInputs(
                                List.of(meterReading1, meterReading2, meterReading3),
                                List.of(waterTariff),
                                List.of(heatingBasicCost),
                                List.of(heatingConsumableTariffs)));

        assertThat(hotWaterSection.name()).isEqualTo("Hot water");
        assertThat(hotWaterSection.readings().size()).isEqualTo(4);
        assertThat(hotWaterSection.totalAmount()).isEqualTo("24124.94");

        WaterReading waterReading1 = hotWaterSection.readings().getFirst();
        assertThat(waterReading1.meterId()).isEqualTo("kitchen");
        assertThat(waterReading1.dateRange())
                .isEqualTo(createDateRange("2024-01-01", "2024-02-01"));
        assertThat(waterReading1.startState()).isEqualTo("93.750");
        assertThat(waterReading1.endState()).isEqualTo("100");
        assertThat(waterReading1.consumption()).isEqualTo("6.250");

        WaterReading waterReading2 = hotWaterSection.readings().get(1);
        assertThat(waterReading2.meterId()).isEqualTo("kitchen");
        assertThat(waterReading2.dateRange())
                .isEqualTo(createDateRange("2024-02-01", "2024-06-01"));
        assertThat(waterReading2.startState()).isEqualTo("100");
        assertThat(waterReading2.endState()).isEqualTo("125");
        assertThat(waterReading2.consumption()).isEqualTo("25");

        WaterReading waterReading3 = hotWaterSection.readings().get(2);
        assertThat(waterReading3.meterId()).isEqualTo("kitchen");
        assertThat(waterReading3.dateRange())
                .isEqualTo(createDateRange("2024-06-01", "2024-09-01"));
        assertThat(waterReading3.startState()).isEqualTo("125");
        assertThat(waterReading3.endState()).isEqualTo("150");
        assertThat(waterReading3.consumption()).isEqualTo("25");

        WaterReading waterReading4 = hotWaterSection.readings().getLast();
        assertThat(waterReading4.meterId()).isEqualTo("kitchen");
        assertThat(waterReading4.dateRange())
                .isEqualTo(createDateRange("2024-09-01", "2025-01-01"));
        assertThat(waterReading4.startState()).isEqualTo("150");
        assertThat(waterReading4.endState()).isEqualTo("183.333");
        assertThat(waterReading4.consumption()).isEqualTo("33.333");
    }

    @Test
    void hotWaterSection_withOneMeterId_withOutOfReportReadings_should_haveCorrectWaterReading() {
        MeterReading meterReading1 = createMeterReading("kitchen", "2023-12-01", "100");
        MeterReading meterReading2 = createMeterReading("kitchen", "2024-06-01", "125");
        MeterReading meterReading3 = createMeterReading("kitchen", "2025-02-01", "150");

        WaterTariff waterTariff =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        ServiceCost heatingBasicCost =
                createServiceCost(createDateRange("2024-01-01", "2025-01-01"), "8000");

        WaterTariff heatingConsumableTariffs =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        HotWaterSection hotWaterSection =
                generateHotWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createHotWaterSectionInputs(
                                List.of(meterReading1, meterReading2, meterReading3),
                                List.of(waterTariff),
                                List.of(heatingBasicCost),
                                List.of(heatingConsumableTariffs)));

        assertThat(hotWaterSection.name()).isEqualTo("Hot water");
        assertThat(hotWaterSection.readings().size()).isEqualTo(2);
        assertThat(hotWaterSection.totalAmount()).isEqualTo("15687.44");

        WaterReading waterReading1 = hotWaterSection.readings().getFirst();
        assertThat(waterReading1.meterId()).isEqualTo("kitchen");
        assertThat(waterReading1.dateRange())
                .isEqualTo(createDateRange("2024-01-01", "2024-06-01"));
        assertThat(waterReading1.startState()).isEqualTo("104.167");
        assertThat(waterReading1.endState()).isEqualTo("125");
        assertThat(waterReading1.consumption()).isEqualTo("20.833");

        WaterReading waterReading2 = hotWaterSection.readings().getLast();
        assertThat(waterReading2.meterId()).isEqualTo("kitchen");
        assertThat(waterReading2.dateRange())
                .isEqualTo(createDateRange("2024-06-01", "2025-01-01"));
        assertThat(waterReading2.startState()).isEqualTo("125");
        assertThat(waterReading2.endState()).isEqualTo("146.875");
        assertThat(waterReading2.consumption()).isEqualTo("21.875");
    }

    @Test
    void hotWaterSection_withReplaceMeter_should_haveCorrectWaterReading() {
        MeterReading meterReading1 = createMeterReading("kitchen", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("kitchen", "2024-06-01", "125");
        MeterReading meterReading3 = createMeterReading("kitchen", "2024-06-01", "0");
        MeterReading meterReading4 = createMeterReading("kitchen", "2025-01-01", "30");

        WaterTariff waterTariff =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        ServiceCost heatingBasicCost =
                createServiceCost(createDateRange("2024-01-01", "2025-01-01"), "8000");

        WaterTariff heatingConsumableTariffs =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        HotWaterSection hotWaterSection =
                generateHotWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createHotWaterSectionInputs(
                                List.of(meterReading1, meterReading2, meterReading3, meterReading4),
                                List.of(waterTariff),
                                List.of(heatingBasicCost),
                                List.of(heatingConsumableTariffs)));

        assertThat(hotWaterSection.name()).isEqualTo("Hot water");
        assertThat(hotWaterSection.readings().size()).isEqualTo(2);
        assertThat(hotWaterSection.totalAmount()).isEqualTo("17900.00");

        WaterReading waterReading1 = hotWaterSection.readings().getFirst();
        assertThat(waterReading1.meterId()).isEqualTo("kitchen");
        assertThat(waterReading1.dateRange())
                .isEqualTo(createDateRange("2024-01-01", "2024-06-01"));
        assertThat(waterReading1.startState()).isEqualTo("100");
        assertThat(waterReading1.endState()).isEqualTo("125");
        assertThat(waterReading1.consumption()).isEqualTo("25");

        WaterReading waterReading2 = hotWaterSection.readings().getLast();
        assertThat(waterReading2.meterId()).isEqualTo("kitchen");
        assertThat(waterReading2.dateRange())
                .isEqualTo(createDateRange("2024-06-01", "2025-01-01"));
        assertThat(waterReading2.startState()).isEqualTo("0");
        assertThat(waterReading2.endState()).isEqualTo("30");
        assertThat(waterReading2.consumption()).isEqualTo("30");
    }

    // endregion

    // region price list tests
    @Test
    void hotWaterSection_withOneTariff_should_haveCorrectPriceList() {
        MeterReading meterReading1 = createMeterReading("kitchen", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("kitchen", "2024-06-01", "125");
        MeterReading meterReading3 = createMeterReading("kitchen", "2025-01-01", "150");

        WaterTariff waterTariff =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        ServiceCost heatingBasicCost =
                createServiceCost(createDateRange("2024-01-01", "2025-01-01"), "8000");

        WaterTariff heatingConsumableTariffs =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        HotWaterSection hotWaterSection =
                generateHotWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createHotWaterSectionInputs(
                                List.of(meterReading1, meterReading2, meterReading3),
                                List.of(waterTariff),
                                List.of(heatingBasicCost),
                                List.of(heatingConsumableTariffs)));

        assertThat(hotWaterSection.name()).isEqualTo("Hot water");
        assertThat(hotWaterSection.priceList().size()).isEqualTo(2);
        assertThat(hotWaterSection.totalAmount()).isEqualTo("17000.00");

        WaterFee hotWaterFee1 = hotWaterSection.priceList().getFirst();
        assertThat(hotWaterFee1.dateRange()).isEqualTo(createDateRange("2024-01-01", "2024-06-01"));
        assertThat(hotWaterFee1.unitAmount()).isEqualTo("90");
        assertThat(hotWaterFee1.quantity()).isEqualTo("25.000");
        assertThat(hotWaterFee1.periodAmount()).isEqualTo("2250.00");

        WaterFee hotWaterFee2 = hotWaterSection.priceList().getLast();
        assertThat(hotWaterFee2.dateRange()).isEqualTo(createDateRange("2024-06-01", "2025-01-01"));
        assertThat(hotWaterFee2.unitAmount()).isEqualTo("90");
        assertThat(hotWaterFee2.quantity()).isEqualTo("25.000");
        assertThat(hotWaterFee2.periodAmount()).isEqualTo("2250.00");
    }

    @Test
    void hotWaterSection_withMultipleTariff_should_haveCorrectPriceList() {
        MeterReading meterReading1 = createMeterReading("kitchen", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("kitchen", "2024-06-01", "125");
        MeterReading meterReading3 = createMeterReading("kitchen", "2025-01-01", "150");

        WaterTariff waterTariff1 =
                createWaterTariff(createDateRange("2024-01-01", "2024-06-01"), "90");
        WaterTariff waterTariff2 =
                createWaterTariff(createDateRange("2024-06-01", "2025-01-01"), "100");

        ServiceCost heatingBasicCost =
                createServiceCost(createDateRange("2024-01-01", "2025-01-01"), "8000");

        WaterTariff heatingConsumableTariffs =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        HotWaterSection hotWaterSection =
                generateHotWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createHotWaterSectionInputs(
                                List.of(meterReading1, meterReading2, meterReading3),
                                List.of(waterTariff1, waterTariff2),
                                List.of(heatingBasicCost),
                                List.of(heatingConsumableTariffs)));

        assertThat(hotWaterSection.name()).isEqualTo("Hot water");
        assertThat(hotWaterSection.priceList().size()).isEqualTo(2);
        assertThat(hotWaterSection.totalAmount()).isEqualTo("17250.00");

        WaterFee hotWaterFee1 = hotWaterSection.priceList().getFirst();
        assertThat(hotWaterFee1.dateRange()).isEqualTo(createDateRange("2024-01-01", "2024-06-01"));
        assertThat(hotWaterFee1.unitAmount()).isEqualTo("90");
        assertThat(hotWaterFee1.quantity()).isEqualTo("25.000");
        assertThat(hotWaterFee1.periodAmount()).isEqualTo("2250.00");

        WaterFee hotWaterFee2 = hotWaterSection.priceList().getLast();
        assertThat(hotWaterFee2.dateRange()).isEqualTo(createDateRange("2024-06-01", "2025-01-01"));
        assertThat(hotWaterFee2.unitAmount()).isEqualTo("100");
        assertThat(hotWaterFee2.quantity()).isEqualTo("25.000");
        assertThat(hotWaterFee2.periodAmount()).isEqualTo("2500.00");
    }

    // endregion

    // region heating basic parts tests
    @Test
    void hotWaterSection_withOneHeatingBasisCost_should_haveCorrectHeatingBasicParts() {
        MeterReading meterReading1 = createMeterReading("kitchen", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("kitchen", "2024-06-01", "125");
        MeterReading meterReading3 = createMeterReading("kitchen", "2025-01-01", "150");

        WaterTariff waterTariff =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        ServiceCost heatingBasicCost1 =
                createServiceCost(createDateRange("2024-01-01", "2025-01-01"), "8000");

        WaterTariff heatingConsumableTariffs =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        HotWaterSection hotWaterSection =
                generateHotWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createHotWaterSectionInputs(
                                List.of(meterReading1, meterReading2, meterReading3),
                                List.of(waterTariff),
                                List.of(heatingBasicCost1),
                                List.of(heatingConsumableTariffs)));

        assertThat(hotWaterSection.name()).isEqualTo("Hot water");
        assertThat(hotWaterSection.heatingBasicParts().size()).isEqualTo(1);
        assertThat(hotWaterSection.totalAmount()).isEqualTo("17000.00");

        WaterHeatingBasicPart waterHeatingBasicPart =
                hotWaterSection.heatingBasicParts().getFirst();
        assertThat(waterHeatingBasicPart.dateRange())
                .isEqualTo(createDateRange("2024-01-01", "2025-01-01"));
        assertThat(waterHeatingBasicPart.numberOfMonths()).isEqualTo("12.00");
        assertThat(waterHeatingBasicPart.monthlyCost()).isEqualTo("666.67");
        assertThat(waterHeatingBasicPart.totalAmount()).isEqualTo("8000.00");
    }

    @Test
    void hotWaterSection_withTwoHeatingBasisCost_should_haveCorrectHeatingBasicParts() {
        MeterReading meterReading1 = createMeterReading("kitchen", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("kitchen", "2024-06-01", "125");
        MeterReading meterReading3 = createMeterReading("kitchen", "2025-01-01", "150");

        WaterTariff waterTariff =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        ServiceCost heatingBasicCost1 =
                createServiceCost(createDateRange("2023-02-01", "2024-02-01"), "8000");

        ServiceCost heatingBasicCost2 =
                createServiceCost(createDateRange("2024-02-01", "2025-02-01"), "8100");

        WaterTariff heatingConsumableTariffs =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        HotWaterSection hotWaterSection =
                generateHotWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createHotWaterSectionInputs(
                                List.of(meterReading1, meterReading2, meterReading3),
                                List.of(waterTariff),
                                List.of(heatingBasicCost1, heatingBasicCost2),
                                List.of(heatingConsumableTariffs)));

        assertThat(hotWaterSection.name()).isEqualTo("Hot water");
        assertThat(hotWaterSection.heatingBasicParts().size()).isEqualTo(2);
        assertThat(hotWaterSection.totalAmount()).isEqualTo("17091.67");

        WaterHeatingBasicPart waterHeatingBasicPart1 =
                hotWaterSection.heatingBasicParts().getFirst();
        assertThat(waterHeatingBasicPart1.dateRange())
                .isEqualTo(createDateRange("2024-01-01", "2024-02-01"));
        assertThat(waterHeatingBasicPart1.numberOfMonths()).isEqualTo("1.00");
        assertThat(waterHeatingBasicPart1.monthlyCost()).isEqualTo("666.67");
        assertThat(waterHeatingBasicPart1.totalAmount()).isEqualTo("666.67");

        WaterHeatingBasicPart waterHeatingBasicPart2 =
                hotWaterSection.heatingBasicParts().getLast();
        assertThat(waterHeatingBasicPart2.dateRange())
                .isEqualTo(createDateRange("2024-02-01", "2025-01-01"));
        assertThat(waterHeatingBasicPart2.numberOfMonths()).isEqualTo("11.00");
        assertThat(waterHeatingBasicPart2.monthlyCost()).isEqualTo("675.00");
        assertThat(waterHeatingBasicPart2.totalAmount()).isEqualTo("7425.00");
    }

    // endregion

    // region heating consumable parts tests
    @Test
    void hotWaterSection_withOneConsumableTariff_should_haveCorrectHeatingConsumableParts() {
        MeterReading meterReading1 = createMeterReading("kitchen", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("kitchen", "2024-06-01", "125");
        MeterReading meterReading3 = createMeterReading("kitchen", "2025-01-01", "150");

        WaterTariff waterTariff =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        ServiceCost heatingBasicCost =
                createServiceCost(createDateRange("2024-01-01", "2025-01-01"), "8772");

        WaterTariff heatingConsumableTariffs =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        HotWaterSection hotWaterSection =
                generateHotWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createHotWaterSectionInputs(
                                List.of(meterReading1, meterReading2, meterReading3),
                                List.of(waterTariff),
                                List.of(heatingBasicCost),
                                List.of(heatingConsumableTariffs)));

        assertThat(hotWaterSection.name()).isEqualTo("Hot water");
        assertThat(hotWaterSection.heatingConsumableParts().size()).isEqualTo(2);
        assertThat(hotWaterSection.totalAmount()).isEqualTo("17772.00");

        WaterHeatingConsumablePart waterHeatingConsumablePart1 =
                hotWaterSection.heatingConsumableParts().getFirst();
        assertThat(waterHeatingConsumablePart1.dateRange())
                .isEqualTo(createDateRange("2024-01-01", "2024-06-01"));
        assertThat(waterHeatingConsumablePart1.unitAmount()).isEqualTo("25.000");
        assertThat(waterHeatingConsumablePart1.unitCost()).isEqualTo("90");
        assertThat(waterHeatingConsumablePart1.totalCost()).isEqualTo("2250.00");

        WaterHeatingConsumablePart waterHeatingConsumablePart2 =
                hotWaterSection.heatingConsumableParts().getLast();
        assertThat(waterHeatingConsumablePart2.dateRange())
                .isEqualTo(createDateRange("2024-06-01", "2025-01-01"));
        assertThat(waterHeatingConsumablePart2.unitAmount()).isEqualTo("25.000");
        assertThat(waterHeatingConsumablePart2.unitCost()).isEqualTo("90");
        assertThat(waterHeatingConsumablePart2.totalCost()).isEqualTo("2250.00");
    }

    @Test
    void hotWaterSection_withTwoConsumableTariffs_should_haveCorrectHeatingConsumableParts() {
        MeterReading meterReading1 = createMeterReading("kitchen", "2024-01-01", "100");
        MeterReading meterReading2 = createMeterReading("kitchen", "2024-06-01", "125");
        MeterReading meterReading3 = createMeterReading("kitchen", "2025-01-01", "150");

        WaterTariff waterTariff =
                createWaterTariff(createDateRange("2024-01-01", "2025-01-01"), "90");

        ServiceCost heatingBasicCost =
                createServiceCost(createDateRange("2024-01-01", "2025-01-01"), "8000");

        WaterTariff heatingConsumableTariffs1 =
                createWaterTariff(createDateRange("2023-02-01", "2024-02-01"), "90");

        WaterTariff heatingConsumableTariffs2 =
                createWaterTariff(createDateRange("2024-02-01", "2025-02-01"), "100");

        HotWaterSection hotWaterSection =
                generateHotWaterSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createHotWaterSectionInputs(
                                List.of(meterReading1, meterReading2, meterReading3),
                                List.of(waterTariff),
                                List.of(heatingBasicCost),
                                List.of(heatingConsumableTariffs1, heatingConsumableTariffs2)));

        assertThat(hotWaterSection.name()).isEqualTo("Hot water");
        assertThat(hotWaterSection.heatingConsumableParts().size()).isEqualTo(3);
        assertThat(hotWaterSection.totalAmount()).isEqualTo("17450.00");

        WaterHeatingConsumablePart waterHeatingConsumablePart1 =
                hotWaterSection.heatingConsumableParts().getFirst();
        assertThat(waterHeatingConsumablePart1.dateRange())
                .isEqualTo(createDateRange("2024-01-01", "2024-02-01"));
        assertThat(waterHeatingConsumablePart1.unitAmount()).isEqualTo("5.000");
        assertThat(waterHeatingConsumablePart1.unitCost()).isEqualTo("90");
        assertThat(waterHeatingConsumablePart1.totalCost()).isEqualTo("450.00");

        WaterHeatingConsumablePart waterHeatingConsumablePart2 =
                hotWaterSection.heatingConsumableParts().get(1);
        assertThat(waterHeatingConsumablePart2.dateRange())
                .isEqualTo(createDateRange("2024-02-01", "2024-06-01"));
        assertThat(waterHeatingConsumablePart2.unitAmount()).isEqualTo("20.000");
        assertThat(waterHeatingConsumablePart2.unitCost()).isEqualTo("100");
        assertThat(waterHeatingConsumablePart2.totalCost()).isEqualTo("2000.00");

        WaterHeatingConsumablePart waterHeatingConsumablePart3 =
                hotWaterSection.heatingConsumableParts().getLast();
        assertThat(waterHeatingConsumablePart3.dateRange())
                .isEqualTo(createDateRange("2024-06-01", "2025-01-01"));
        assertThat(waterHeatingConsumablePart3.unitAmount()).isEqualTo("25.000");
        assertThat(waterHeatingConsumablePart3.unitCost()).isEqualTo("100");
        assertThat(waterHeatingConsumablePart3.totalCost()).isEqualTo("2500.00");
    }
    // endregion
}
