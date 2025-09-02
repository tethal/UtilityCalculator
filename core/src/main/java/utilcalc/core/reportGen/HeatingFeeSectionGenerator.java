package utilcalc.core.reportGen;

import static utilcalc.core.reportGen.ReportGenUtil.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.time.YearMonth;
import java.util.*;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.HeatingFeeInputs;
import utilcalc.core.model.input.ServiceCost;
import utilcalc.core.model.output.HeatingFee;
import utilcalc.core.model.output.HeatingFeeSection;

final class HeatingFeeSectionGenerator {
    private static final int DISPLAY_DECIMAL_PLACES = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final Map<Month, BigDecimal> COEFFICIENTS =
            new EnumMap<>(
                    Map.ofEntries(
                            Map.entry(Month.JANUARY, new BigDecimal("0.19")),
                            Map.entry(Month.FEBRUARY, new BigDecimal("0.16")),
                            Map.entry(Month.MARCH, new BigDecimal("0.14")),
                            Map.entry(Month.APRIL, new BigDecimal("0.09")),
                            Map.entry(Month.MAY, new BigDecimal("0.02")),
                            Map.entry(Month.JUNE, new BigDecimal("0.0")),
                            Map.entry(Month.JULY, new BigDecimal("0.0")),
                            Map.entry(Month.AUGUST, new BigDecimal("0.0")),
                            Map.entry(Month.SEPTEMBER, new BigDecimal("0.01")),
                            Map.entry(Month.OCTOBER, new BigDecimal("0.08")),
                            Map.entry(Month.NOVEMBER, new BigDecimal("0.14")),
                            Map.entry(Month.DECEMBER, new BigDecimal("0.17"))));

    private HeatingFeeSectionGenerator() {}

    static HeatingFeeSection generateHeatingFeeSection(
            DateRange reportDateRange, HeatingFeeInputs heatingFeeInputs) {

        String name = heatingFeeInputs.name();

        List<ServiceCost> partialServiceCost =
                splitServiceCostsByReport(reportDateRange, heatingFeeInputs.heatingFees());

        List<ServiceCost> monthlyServiceCost = splitServiceCostsByMonths(partialServiceCost);

        List<HeatingFee> heatingFees =
                monthlyServiceCost.stream()
                        .map(HeatingFeeSectionGenerator::calculateHeatingFee)
                        .toList();

        BigDecimal totalAmount =
                calculateAmount(heatingFees, HeatingFee::feeAmount)
                        .setScale(DISPLAY_DECIMAL_PLACES, ROUNDING_MODE);

        return new HeatingFeeSection(name, totalAmount, toRoundedFees(heatingFees));
    }

    static HeatingFee calculateHeatingFee(ServiceCost serviceCost) {
        DateRange serviceCostDateRange = serviceCost.dateRange();
        if (!serviceCostDateRange.isSingleMonth()) {
            throw new IllegalArgumentException(
                    "Date range of service cost must be within a single month");
        }

        BigDecimal monthCount = serviceCost.dateRange().getMonthCount();
        BigDecimal annualCost = serviceCost.annualCost();
        YearMonth yearMonth = YearMonth.from(serviceCost.dateRange().startDate());

        BigDecimal coefficient = COEFFICIENTS.get(yearMonth.getMonth());
        BigDecimal feeAmount = annualCost.multiply(monthCount).multiply(coefficient);

        return new HeatingFee(yearMonth, annualCost, monthCount, coefficient, feeAmount);
    }

    private static List<HeatingFee> toRoundedFees(List<HeatingFee> heatingFees) {
        return heatingFees.stream().map(HeatingFeeSectionGenerator::toRoundedFee).toList();
    }

    private static HeatingFee toRoundedFee(HeatingFee heatingFee) {
        return new HeatingFee(
                heatingFee.yearMonth(),
                heatingFee.annualCost(),
                heatingFee.monthCount().setScale(DISPLAY_DECIMAL_PLACES, ROUNDING_MODE),
                heatingFee.coefficient(),
                heatingFee.feeAmount().setScale(DISPLAY_DECIMAL_PLACES, ROUNDING_MODE));
    }
}
