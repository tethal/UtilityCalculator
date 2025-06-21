package utilcalc.core.reportGen;

import static utilcalc.core.model.OpenIntervalFactory.*;
import static utilcalc.core.reportGen.OtherFeeSectionGenerator.generateOtherFeeSection;
import static utilcalc.core.reportGen.ServiceCostFactory.*;

import java.util.List;
import utilcalc.core.model.OpenInterval;
import utilcalc.core.model.input.OtherFeeInputs;
import utilcalc.core.model.input.ServiceCost;
import utilcalc.core.model.output.OtherFeeSection;

final class OtherFeeSectionFactory {
    private static final OpenInterval FULL_2024_INTERVAL = full2024Interval();
    private static final OpenInterval FULL_2025_INTERVAL = full2025Interval();
    private static final ServiceCost FULL_2024_SERVICE_COST = valid2024ServiceCost();
    private static final ServiceCost FULL_2025_SERVICE_COST = valid2025ServiceCost();
    private static final ServiceCost PARTIAL_SERVICE_COST = partialServiceCost();

    /**
     * ServiceCost: [ 1.1.2024 ; 1.1.2025) cost: 8772
     *
     * <p>DateInterval: [ 1.1.2024 ; 1.1.2025 )
     */
    public static OtherFeeSection createOneYearOtherFeeSection() {
        return generateOtherFeeSection(
                createOtherFeeInputs(FULL_2024_SERVICE_COST),
                FULL_2024_INTERVAL.startDate(),
                FULL_2024_INTERVAL.endDateExclusive());
    }

    /**
     * ServiceCost 1: [ 1.1.2024 ; 1.1.2025) cost: 8772
     *
     * <p>ServiceCost 2: [ 1.1.2025 ; 1.1.2026) cost: 8000
     *
     * <p>DateInterval: [ 1.1.2024 ; 1.1.2025 )
     */
    public static OtherFeeSection createTwoYearOtherFeeSection() {
        return generateOtherFeeSection(
                createOtherFeeInputs(FULL_2024_SERVICE_COST, FULL_2025_SERVICE_COST),
                FULL_2024_INTERVAL.startDate(),
                FULL_2025_INTERVAL.endDateExclusive());
    }

    /**
     * ServiceCost: [ 1.1.2024 ; 1.1.2025) cost: 8772
     *
     * <p>DateInterval: [ 15.1.2024 ; 25.1.2024 )
     */
    public static OtherFeeSection createPartialMonthOtherFeeSection() {
        OpenInterval partialMonthInterval = partialMonthInterval();

        return generateOtherFeeSection(
                createOtherFeeInputs(FULL_2024_SERVICE_COST),
                partialMonthInterval.startDate(),
                partialMonthInterval.endDateExclusive());
    }

    /**
     * ServiceCost 1: [ 1.1.2024 ; 1.1.2025) cost: 8772
     *
     * <p>ServiceCost 2: [ 1.1.2025 ; 1.1.2026) cost: 8000
     *
     * <p>DateInterval: [ 15.1.2024 ; 15.1.2025 )
     */
    public static OtherFeeSection createPartialMonthOverTwoYearsOtherFeeSection() {
        OpenInterval twoPartialMonthsInterval = twoPartialMonthsInterval();

        return generateOtherFeeSection(
                createOtherFeeInputs(FULL_2024_SERVICE_COST, FULL_2025_SERVICE_COST),
                twoPartialMonthsInterval.startDate(),
                twoPartialMonthsInterval.endDateExclusive());
    }

    public static void invalidServiceCostCoverageOtherFeeSection() {
        generateOtherFeeSection(
                createOtherFeeInputs(PARTIAL_SERVICE_COST),
                FULL_2024_INTERVAL.startDate(),
                FULL_2024_INTERVAL.endDateExclusive());
    }

    public static void overlapServiceCostOtherFeeSection() {
        ServiceCost partialServiceCost = partialServiceCost();

        generateOtherFeeSection(
                createOtherFeeInputs(partialServiceCost, FULL_2025_SERVICE_COST),
                partialServiceCost.startDate(),
                FULL_2025_INTERVAL.endDateExclusive());
    }

    private static OtherFeeInputs createOtherFeeInputs(ServiceCost... serviceCosts) {
        return new OtherFeeInputs("Other fees", List.of(serviceCosts));
    }
}
