package utilcalc.core.reportGen;

import static utilcalc.core.reportGen.ReportGenUtil.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
            DateRange reportDateRange, OtherFeeInputs otherFeeInputs) {

        String name = otherFeeInputs.name();
        List<ServiceCost> partialServiceCost =
                splitServiceCostsByReport(reportDateRange, otherFeeInputs.otherFees());

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
        DateRange dateRange = serviceCost.dateRange();
        BigDecimal monthlyCost =
                serviceCost
                        .annualCost()
                        .divide(MONTHS_IN_YEAR, CALCULATION_SCALE, RoundingMode.HALF_UP);
        BigDecimal mountCount = dateRange.getMonthCount();
        BigDecimal feeAmount =
                mountCount
                        .multiply(monthlyCost)
                        .setScale(DISPLAY_DECIMAL_PLACES, RoundingMode.HALF_UP);

        BigDecimal roundMonthlyCost =
                monthlyCost.setScale(DISPLAY_DECIMAL_PLACES, RoundingMode.HALF_UP);
        BigDecimal roundMonthCount =
                mountCount.setScale(DISPLAY_DECIMAL_PLACES, RoundingMode.HALF_UP);

        return new OtherFee(dateRange, roundMonthlyCost, roundMonthCount, feeAmount);
    }
}
