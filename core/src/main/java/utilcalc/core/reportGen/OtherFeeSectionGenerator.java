package utilcalc.core.reportGen;

import static utilcalc.core.reportGen.ReportGenUtil.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.OtherFeeInputs;
import utilcalc.core.model.input.ServiceCost;
import utilcalc.core.model.output.OtherFee;
import utilcalc.core.model.output.OtherFeeSection;

final class OtherFeeSectionGenerator {
    private static final int DISPLAY_DECIMAL_PLACES = 2;
    private static final int CALCULATION_SCALE = 10;
    private static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);

    private OtherFeeSectionGenerator() {}

    static OtherFeeSection generateOtherFeeSection(
            OtherFeeInputs otherFeeInputs, LocalDate reportStartDate, LocalDate reportEndDate) {
        validateServiceCostCoverage(reportStartDate, reportEndDate, otherFeeInputs.otherFees());

        String name = otherFeeInputs.name();
        List<ServiceCost> serviceCosts = otherFeeInputs.otherFees();
        List<ServiceCost> partialServiceCost =
                splitServiceCostsByReport(reportStartDate, reportEndDate, serviceCosts);

        List<OtherFee> otherFees =
                partialServiceCost.stream()
                        .map(OtherFeeSectionGenerator::calculateOtherFee)
                        .toList();

        BigDecimal totalAmount =
                otherFees.stream()
                        .map(OtherFee::feeAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OtherFeeSection(name, totalAmount, otherFees);
    }

    private static OtherFee calculateOtherFee(ServiceCost serviceCost) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        LocalDate startDate = serviceCost.startDate();
        LocalDate endDate = serviceCost.endDate();
        String description =
                String.format(
                        "%s - %s",
                        startDate.format(formatter), endDate.minusDays(1).format(formatter));

        DateRange dateRange = new DateRange(startDate, endDate);

        BigDecimal annualCost = serviceCost.annualCost();
        BigDecimal notRoundMountCount = dateRange.getMonthCount();
        BigDecimal monthCount =
                notRoundMountCount.setScale(DISPLAY_DECIMAL_PLACES, RoundingMode.HALF_UP);
        BigDecimal unitAmount =
                annualCost.divide(MONTHS_IN_YEAR, CALCULATION_SCALE, RoundingMode.HALF_UP);
        BigDecimal feeAmount =
                notRoundMountCount
                        .multiply(unitAmount)
                        .setScale(DISPLAY_DECIMAL_PLACES, RoundingMode.HALF_UP);

        return new OtherFee(description, annualCost, monthCount, feeAmount);
    }
}
