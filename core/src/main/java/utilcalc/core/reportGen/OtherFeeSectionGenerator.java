package utilcalc.core.reportGen;

import static utilcalc.core.reportGen.ReportGenUtil.*;

import java.math.BigDecimal;
import java.util.List;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.OtherFeeInputs;
import utilcalc.core.model.output.OtherFee;
import utilcalc.core.model.output.OtherFeeSection;

final class OtherFeeSectionGenerator {

    private OtherFeeSectionGenerator() {}

    static OtherFeeSection generateOtherFeeSection(
            DateRange reportDateRange, OtherFeeInputs otherFeeInputs) {

        String name = otherFeeInputs.name();

        List<OtherFee> otherFees =
                calculateFees(reportDateRange, otherFeeInputs.otherFees()).stream()
                        .map(OtherFeeSectionGenerator::mapFeeResultToOtherFee)
                        .toList();

        BigDecimal totalAmount =
                otherFees.stream()
                        .map(OtherFee::feeAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OtherFeeSection(name, totalAmount, otherFees);
    }

    private static OtherFee mapFeeResultToOtherFee(FeeCalculationResult feeCalculationResult) {
        return new OtherFee(
                feeCalculationResult.dateRange(),
                feeCalculationResult.monthlyCost(),
                feeCalculationResult.monthCount(),
                feeCalculationResult.feeAmount());
    }
}
