package utilcalc.core.reportGen;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import utilcalc.core.model.input.ServiceCost;

final class ReportGenUtil {

    private ReportGenUtil() {}

    static List<ServiceCost> splitServiceCostsByReport(
            LocalDate reportStartDate, LocalDate reportEndDate, List<ServiceCost> serviceCosts) {
        List<ServiceCost> result = new ArrayList<>();

        for (ServiceCost cost : serviceCosts) {
            LocalDate costStartDate = cost.startDate();
            LocalDate costEndDate = cost.endDate();

            LocalDate overlapStart =
                    reportStartDate.isAfter(costStartDate) ? reportStartDate : costStartDate;
            LocalDate overlapEnd =
                    reportEndDate.isBefore(costEndDate) ? reportEndDate : costEndDate;

            if (overlapStart.isBefore(overlapEnd)) {
                result.add(new ServiceCost(overlapStart, overlapEnd, cost.annualCost()));
            }
        }

        return result;
    }

    static void validateServiceCostCoverage(
            LocalDate reportStartDate, LocalDate reportEndDate, List<ServiceCost> serviceCosts) {

        List<ServiceCost> sortedServiceCosts = new ArrayList<>(serviceCosts);
        sortedServiceCosts.sort(Comparator.comparing(ServiceCost::startDate));

        if (sortedServiceCosts.getFirst().startDate().isAfter(reportStartDate)
                || sortedServiceCosts.getLast().endDate().isBefore(reportEndDate)) {
            throw new IllegalArgumentException(
                    "ServiceCosts do not fully cover the report date interval.");
        }

        for (int i = 0; i < sortedServiceCosts.size() - 1; i++) {
            LocalDate currentEnd = sortedServiceCosts.get(i).endDate();
            LocalDate nextStart = sortedServiceCosts.get(i + 1).startDate();

            if (!currentEnd.equals(nextStart)) {
                throw new IllegalArgumentException(
                        "ServiceCosts do not connect seamlessly or they overlap: "
                                + sortedServiceCosts.get(i)
                                + " and "
                                + sortedServiceCosts.get(i + 1));
            }
        }
    }
}
