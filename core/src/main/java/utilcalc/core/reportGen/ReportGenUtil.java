package utilcalc.core.reportGen;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.ServiceCost;

final class ReportGenUtil {

    private ReportGenUtil() {}

    static List<ServiceCost> splitServiceCostsByReport(
            DateRange reportDateRange, List<ServiceCost> serviceCosts) {

        validateDateRangeCoverage(
                reportDateRange, serviceCosts, ServiceCost::dateRange, "ServiceCosts");

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
            DateRange reportDateRange,
            List<T> items,
            Function<T, DateRange> dateRangeExtractor,
            String itemName) {

        List<T> sortedItems =
                items.stream()
                        .sorted(
                                Comparator.comparing(
                                        item -> dateRangeExtractor.apply(item).startDate()))
                        .toList();

        LocalDate sortedFirstStart = dateRangeExtractor.apply(sortedItems.getFirst()).startDate();
        LocalDate sortedLastEnd =
                dateRangeExtractor.apply(sortedItems.getLast()).endDateExclusive();

        if (sortedFirstStart.isAfter(reportDateRange.startDate())
                || sortedLastEnd.isBefore(reportDateRange.endDateExclusive())) {
            throw new IllegalArgumentException(
                    itemName + " do not fully cover the report date interval.");
        }

        for (int i = 0; i < sortedItems.size() - 1; i++) {
            LocalDate currentEnd = dateRangeExtractor.apply(sortedItems.get(i)).endDateExclusive();
            LocalDate nextStart = dateRangeExtractor.apply(sortedItems.get(i + 1)).startDate();

            if (!currentEnd.equals(nextStart)) {
                throw new IllegalArgumentException(
                        String.format(
                                "%s do not connect seamlessly or they overlap: %s and %s",
                                itemName, sortedItems.get(i), sortedItems.get(i + 1)));
            }
        }
    }
}
