package utilcalc.core.reportGen;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.ServiceCost;

final class ReportGenUtil {

    private static final int DISPLAY_DECIMAL_PLACES = 2;
    private static final int CALCULATION_SCALE = 10;
    private static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);

    private ReportGenUtil() {}

    static List<ServiceCost> splitServiceCostsByReport(DateRange reportDateRange, List<ServiceCost> serviceCosts) {

        validateDateRangeCoverage(reportDateRange, serviceCosts, ServiceCost::dateRange, "ServiceCosts");

        List<ServiceCost> result = new ArrayList<>();

        for (ServiceCost cost : serviceCosts) {
            cost.dateRange()
                    .intersect(reportDateRange)
                    .ifPresent(overlap -> result.add(new ServiceCost(overlap, cost.annualCost())));
        }

        return result;
    }

    static List<ServiceCost> splitServiceCostsByMonths(List<ServiceCost> serviceCosts) {
        List<ServiceCost> result = new ArrayList<>();

        for (ServiceCost cost : serviceCosts) {
            List<DateRange> months = cost.dateRange().splitByMonths();

            for (DateRange monthRange : months) {
                result.add(new ServiceCost(monthRange, cost.annualCost()));
            }
        }

        return result;
    }

    static <T> void validateDateRangeCoverage(
            DateRange reportDateRange, List<T> items, Function<T, DateRange> dateRangeExtractor, String itemName) {

        List<T> sortedItems = items.stream()
                .sorted(Comparator.comparing(
                        item -> dateRangeExtractor.apply(item).startDate()))
                .toList();

        LocalDate sortedFirstStart =
                dateRangeExtractor.apply(sortedItems.getFirst()).startDate();
        LocalDate sortedLastEnd =
                dateRangeExtractor.apply(sortedItems.getLast()).endDateExclusive();

        if (sortedFirstStart.isAfter(reportDateRange.startDate())
                || sortedLastEnd.isBefore(reportDateRange.endDateExclusive())) {
            throw new IllegalArgumentException(itemName + " do not fully cover the report date interval.");
        }

        for (int i = 0; i < sortedItems.size() - 1; i++) {
            LocalDate currentEnd = dateRangeExtractor.apply(sortedItems.get(i)).endDateExclusive();
            LocalDate nextStart =
                    dateRangeExtractor.apply(sortedItems.get(i + 1)).startDate();

            if (!currentEnd.equals(nextStart)) {
                throw new IllegalArgumentException(String.format(
                        "%s do not connect seamlessly or they overlap: %s and %s",
                        itemName, sortedItems.get(i), sortedItems.get(i + 1)));
            }
        }
    }

    static <T> BigDecimal calculateAmount(List<T> items, Function<T, BigDecimal> itemsAmountExtractor) {
        return items.stream().map(itemsAmountExtractor).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    static List<FeeCalculationResult> calculateFees(DateRange reportDateRange, List<ServiceCost> serviceCosts) {
        return splitServiceCostsByReport(reportDateRange, serviceCosts).stream()
                .map(ReportGenUtil::calculateFee)
                .toList();
    }

    private static FeeCalculationResult calculateFee(ServiceCost serviceCost) {
        DateRange dateRange = serviceCost.dateRange();
        BigDecimal monthlyCost =
                serviceCost.annualCost().divide(MONTHS_IN_YEAR, CALCULATION_SCALE, RoundingMode.HALF_UP);
        BigDecimal mountCount = dateRange.getMonthCount();
        BigDecimal feeAmount = mountCount.multiply(monthlyCost).setScale(DISPLAY_DECIMAL_PLACES, RoundingMode.HALF_UP);

        BigDecimal roundMonthlyCost = monthlyCost.setScale(DISPLAY_DECIMAL_PLACES, RoundingMode.HALF_UP);
        BigDecimal roundMonthCount = mountCount.setScale(DISPLAY_DECIMAL_PLACES, RoundingMode.HALF_UP);

        return new FeeCalculationResult(dateRange, roundMonthCount, roundMonthlyCost, feeAmount);
    }

    public record FeeCalculationResult(
            DateRange dateRange, BigDecimal monthCount, BigDecimal monthlyCost, BigDecimal feeAmount) {}
}
