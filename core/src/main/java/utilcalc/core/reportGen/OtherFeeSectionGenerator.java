package utilcalc.core.reportGen;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import utilcalc.core.model.input.OtherFeeInputs;
import utilcalc.core.model.input.ServiceCost;
import utilcalc.core.model.output.OtherFee;
import utilcalc.core.model.output.OtherFeeSection;

final class OtherFeeSectionGenerator {
    private static final int DISPLAY_DECIMAL_PLACES = 2;
    private static final int CALCULATION_SCALE = 10;
    private static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);

    private OtherFeeSectionGenerator() {}

    static OtherFeeSection generateOtherFeeSection(OtherFeeInputs otherFeeInputs) {
        String name = otherFeeInputs.name();
        List<ServiceCost> serviceCosts = otherFeeInputs.otherFees();

        List<OtherFee> otherFees =
                serviceCosts.stream()
                        .map(OtherFeeSectionGenerator::mapServiceCostToOtherFee)
                        .toList();

        BigDecimal totalAmount =
                otherFees.stream()
                        .map(OtherFee::feeAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OtherFeeSection(name, totalAmount, otherFees);
    }

    private static OtherFee mapServiceCostToOtherFee(ServiceCost serviceCost) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        String startDate = serviceCost.startDate().format(formatter);
        String endDate = serviceCost.endDate().format(formatter);
        String description = String.format("%s - %s", startDate, endDate);

        BigDecimal annualCost = serviceCost.annualCost();
        BigDecimal notRoundMountCount =
                getMonthCount(serviceCost.startDate(), serviceCost.endDate());
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

    private static BigDecimal getMonthCount(LocalDate startDate, LocalDate endDate) {
        YearMonth startMonth = YearMonth.from(startDate);
        YearMonth endMonth = YearMonth.from(endDate);

        if (startMonth.equals(endMonth)) {
            return calculateSingleMonth(startDate, endDate);
        }

        BigDecimal partialStart = calculatePartialMonth(startDate, true);
        BigDecimal partialEnd = calculatePartialMonth(endDate, false);

        long fullMonths =
                Math.max(0, ChronoUnit.MONTHS.between(startMonth.plusMonths(1), endMonth));

        return BigDecimal.valueOf(fullMonths).add(partialStart).add(partialEnd);
    }

    private static BigDecimal calculateSingleMonth(LocalDate startDate, LocalDate endDate) {
        YearMonth month = YearMonth.from(startDate);
        int daysInMonth = month.lengthOfMonth();

        if (startDate.getDayOfMonth() == 1 && endDate.getDayOfMonth() == daysInMonth) {
            return BigDecimal.ONE;
        }

        int daysBetween = endDate.getDayOfMonth() - startDate.getDayOfMonth() + 1;

        return BigDecimal.valueOf(daysBetween)
                .divide(BigDecimal.valueOf(daysInMonth), CALCULATION_SCALE, RoundingMode.HALF_UP);
    }

    private static BigDecimal calculatePartialMonth(LocalDate date, boolean isStart) {
        int day = date.getDayOfMonth();
        int daysInMonth = YearMonth.from(date).lengthOfMonth();

        int effectiveDays = isStart ? (daysInMonth - day + 1) : day;

        return BigDecimal.valueOf(effectiveDays)
                .divide(BigDecimal.valueOf(daysInMonth), CALCULATION_SCALE, RoundingMode.HALF_UP);
    }
}
