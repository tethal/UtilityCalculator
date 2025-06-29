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

        Map<YearMonth, BigDecimal> countsByMonth = calculateCountsByMonth(partialServiceCost);
        Map<YearMonth, ServiceCost> serviceCostByMonth = mapServiceCostsByMonth(partialServiceCost);
        List<HeatingFee> heatingFees =
                generateHeatingFees(reportDateRange, countsByMonth, serviceCostByMonth);

        BigDecimal totalAmount =
                heatingFees.stream()
                        .map(HeatingFee::feeAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new HeatingFeeSection(name, totalAmount, heatingFees);
    }

    private static Map<YearMonth, BigDecimal> calculateCountsByMonth(List<ServiceCost> costs) {
        return costs.stream()
                .map(c -> c.dateRange().getCountsByMonth())
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, BigDecimal::add));
    }

    private static List<HeatingFee> generateHeatingFees(
            DateRange reportDateRange,
            Map<YearMonth, BigDecimal> countsByMonth,
            Map<YearMonth, ServiceCost> serviceCostsByMonth) {

        List<HeatingFee> heatingFees = new ArrayList<>();
        YearMonth ym = YearMonth.from(reportDateRange.startDate());
        YearMonth endYm = YearMonth.from(reportDateRange.endDateExclusive().minusDays(1));

        while (!ym.isAfter(endYm)) {
            final YearMonth finalYm = ym;

            ServiceCost cost =
                    Optional.ofNullable(serviceCostsByMonth.get(ym))
                            .orElseThrow(
                                    () ->
                                            new NoSuchElementException(
                                                    "No ServiceCost for " + finalYm));

            BigDecimal monthCount = countsByMonth.get(ym);
            heatingFees.add(calculateHeatingFee(ym, monthCount, cost));
            ym = ym.plusMonths(1);
        }

        return heatingFees;
    }

    private static HeatingFee calculateHeatingFee(
            YearMonth yearMonth, BigDecimal monthCount, ServiceCost serviceCost) {
        int displayDecimalPlaces = 2;
        BigDecimal annualCost = serviceCost.annualCost();
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
