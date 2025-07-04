package utilcalc.core.reportGen;

import static utilcalc.core.reportGen.ReportGenUtil.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.HeatingFeeInputs;
import utilcalc.core.model.input.ServiceCost;
import utilcalc.core.model.output.HeatingFee;
import utilcalc.core.model.output.HeatingFeeSection;

final class HeatingFeeSectionGenerator {
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
                        .collect(Collectors.toList());

        BigDecimal totalAmount =
                heatingFees.stream()
                        .map(HeatingFee::feeAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new HeatingFeeSection(name, totalAmount, heatingFees);
    }

    static HeatingFee calculateHeatingFee(ServiceCost serviceCost) {
        DateRange serviceCostDateRange = serviceCost.dateRange();
        if (!serviceCostDateRange.isSingleMonth()) {
            throw new IllegalArgumentException(
                    "Date range of service cost must be within a single month");
        }

        int displayDecimalPlaces = 2;
        BigDecimal monthCount = serviceCost.dateRange().getMonthCount();
        BigDecimal annualCost = serviceCost.annualCost();
        YearMonth yearMonth = YearMonth.from(serviceCost.dateRange().startDate());

        BigDecimal coefficient = COEFFICIENTS.get(yearMonth.getMonth());
        BigDecimal feeAmount =
                annualCost
                        .multiply(monthCount)
                        .multiply(coefficient)
                        .setScale(displayDecimalPlaces, RoundingMode.HALF_UP);

        BigDecimal roundedMonthCount =
                monthCount.setScale(displayDecimalPlaces, RoundingMode.HALF_UP);

        return new HeatingFee(yearMonth, annualCost, roundedMonthCount, coefficient, feeAmount);
    }
}
