package utilcalc.core.reportGen;

import static utilcalc.core.reportGen.ReportGenUtil.calculateAmount;
import static utilcalc.core.reportGen.ReportGenUtil.calculateFees;
import static utilcalc.core.reportGen.WaterSectionUtil.generatePriceList;
import static utilcalc.core.reportGen.WaterSectionUtil.generateWaterReadings;

import java.math.BigDecimal;
import java.util.List;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.HotWaterSectionInputs;
import utilcalc.core.model.output.*;
import utilcalc.core.reportGen.ReportGenUtil.FeeCalculationResult;

final class HotWaterSectionGenerator {

    private HotWaterSectionGenerator() {}

    static HotWaterSection generateHotWaterSection(
            DateRange reportDateRange, HotWaterSectionInputs hotWaterSectionInputs) {

        List<WaterReading> readings =
                generateWaterReadings(reportDateRange, hotWaterSectionInputs.readings());

        List<WaterFee> priceList =
                generatePriceList(reportDateRange, hotWaterSectionInputs.priceList(), readings);

        List<WaterHeatingBasicPart> heatingBasicParts =
                calculateFees(reportDateRange, hotWaterSectionInputs.heatingBasicCosts()).stream()
                        .map(HotWaterSectionGenerator::mapFeeResultToHeatingBasicPart)
                        .toList();

        List<WaterHeatingConsumablePart> heatingConsumableParts =
                generatePriceList(
                                reportDateRange,
                                hotWaterSectionInputs.heatingConsumableTariffs(),
                                readings)
                        .stream()
                        .map(HotWaterSectionGenerator::mapWaterFeeToWaterHeatingConsumablePart)
                        .toList();

        BigDecimal priceListsAmount = calculateAmount(priceList, WaterFee::periodAmount);
        BigDecimal basicPartsAmount =
                calculateAmount(heatingBasicParts, WaterHeatingBasicPart::totalAmount);
        BigDecimal consumablePartsAmount =
                calculateAmount(heatingConsumableParts, WaterHeatingConsumablePart::totalCost);
        BigDecimal totalAmount = priceListsAmount.add(basicPartsAmount).add(consumablePartsAmount);

        return new HotWaterSection(
                hotWaterSectionInputs.name(),
                totalAmount,
                readings,
                priceList,
                heatingBasicParts,
                heatingConsumableParts);
    }

    private static WaterHeatingBasicPart mapFeeResultToHeatingBasicPart(
            FeeCalculationResult feeCalculationResult) {
        return new WaterHeatingBasicPart(
                feeCalculationResult.dateRange(),
                feeCalculationResult.monthCount(),
                feeCalculationResult.monthlyCost(),
                feeCalculationResult.feeAmount());
    }

    private static WaterHeatingConsumablePart mapWaterFeeToWaterHeatingConsumablePart(
            WaterFee waterFee) {
        return new WaterHeatingConsumablePart(
                waterFee.dateRange(),
                waterFee.quantity(),
                waterFee.unitAmount(),
                waterFee.periodAmount());
    }
}
