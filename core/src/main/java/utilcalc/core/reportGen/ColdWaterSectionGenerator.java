package utilcalc.core.reportGen;

import static utilcalc.core.reportGen.ReportGenUtil.validateDateRangeCoverage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.ColdWaterSectionInputs;
import utilcalc.core.model.input.MeterReading;
import utilcalc.core.model.input.WaterTariff;
import utilcalc.core.model.output.ColdWaterFee;
import utilcalc.core.model.output.ColdWaterReading;
import utilcalc.core.model.output.ColdWaterSection;

final class ColdWaterSectionGenerator {

    private ColdWaterSectionGenerator() {}

    static ColdWaterSection generateColdWaterSection(
            DateRange reportDateRange, ColdWaterSectionInputs coldWaterSectionInputs) {

        String name = coldWaterSectionInputs.name();
        List<MeterReading> inputReadings = coldWaterSectionInputs.readings();
        List<WaterTariff> inputWaterTariffs =
                coldWaterSectionInputs.priceList().stream()
                        .sorted(
                                Comparator.comparing(
                                        priceList -> priceList.dateRange().startDate()))
                        .toList();

        List<ColdWaterReading> detailReadings =
                inputReadings.stream()
                        .collect(Collectors.groupingBy(MeterReading::meterId))
                        .values()
                        .stream()
                        .flatMap(group -> createColdWaterReadings(group).stream())
                        .toList();

        validateDateRangeCoverage(
                reportDateRange, inputWaterTariffs, WaterTariff::dateRange, "WaterTariff");

        List<ColdWaterFee> priceList =
                inputWaterTariffs.stream()
                        .flatMap(
                                waterTariff -> {
                                    List<DateRange> validIntervals =
                                            extractValidIntervals(waterTariff, detailReadings);
                                    List<ColdWaterReading> aggregated =
                                            aggregateReadings(detailReadings, validIntervals);
                                    return calculateColdWaterFees(waterTariff, aggregated).stream();
                                })
                        .toList();

        BigDecimal totalAmount =
                priceList.stream()
                        .map(ColdWaterFee::periodAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ColdWaterSection(name, totalAmount, detailReadings, priceList);
    }

    private static List<ColdWaterReading> createColdWaterReadings(List<MeterReading> readings) {

        return IntStream.range(0, readings.size() - 1)
                .mapToObj(
                        i -> {
                            MeterReading current = readings.get(i);
                            MeterReading next = readings.get(i + 1);

                            if (!next.readingDate().isAfter(current.readingDate())) {
                                return null;
                            }

                            boolean isNewMeter = next.state().compareTo(current.state()) < 0;
                            BigDecimal startState = isNewMeter ? BigDecimal.ZERO : current.state();
                            BigDecimal endState = next.state();
                            BigDecimal consumption =
                                    isNewMeter ? endState : endState.subtract(startState);

                            DateRange dateRange =
                                    new DateRange(current.readingDate(), next.readingDate());

                            return new ColdWaterReading(
                                    dateRange,
                                    current.meterId(),
                                    startState,
                                    endState,
                                    consumption);
                        })
                .filter(Objects::nonNull)
                .toList();
    }

    private static List<ColdWaterReading> aggregateReadings(
            List<ColdWaterReading> detailWaterReading, List<DateRange> intervals) {

        return intervals.stream()
                .map(
                        interval -> {
                            BigDecimal totalConsumption =
                                    calculateTotalConsumption(detailWaterReading, interval);
                            return new ColdWaterReading(
                                    interval,
                                    "Aggregated",
                                    BigDecimal.ZERO,
                                    BigDecimal.ZERO,
                                    totalConsumption);
                        })
                .toList();
    }

    private static List<ColdWaterFee> calculateColdWaterFees(
            WaterTariff waterTariff, List<ColdWaterReading> aggregatedReadings) {

        return aggregatedReadings.stream()
                .filter(
                        reading ->
                                waterTariff.dateRange().intersect(reading.dateRange()).isPresent())
                .map(reading -> calculateColdWaterFee(waterTariff, reading))
                .toList();
    }

    private static ColdWaterFee calculateColdWaterFee(
            WaterTariff waterTariff, ColdWaterReading meterReadings) {

        BigDecimal totalQuantity = meterReadings.consumption();
        BigDecimal roundedQuantity = totalQuantity.setScale(3, RoundingMode.HALF_UP);
        BigDecimal periodAmount =
                totalQuantity
                        .multiply(waterTariff.pricePerCubicMeter())
                        .setScale(2, RoundingMode.HALF_UP);

        return new ColdWaterFee(
                meterReadings.dateRange(),
                roundedQuantity,
                waterTariff.pricePerCubicMeter(),
                periodAmount);
    }

    private static List<DateRange> extractValidIntervals(
            WaterTariff waterTariff, List<ColdWaterReading> meterReadings) {

        Set<LocalDate> datePoints =
                meterReadings.stream()
                        .flatMap(
                                reading ->
                                        Stream.of(
                                                reading.dateRange().startDate(),
                                                reading.dateRange().endDateExclusive()))
                        .collect(Collectors.toSet());

        datePoints.add(waterTariff.dateRange().startDate());
        datePoints.add(waterTariff.dateRange().endDateExclusive());

        List<LocalDate> sortedDates = datePoints.stream().sorted().toList();

        return IntStream.range(0, sortedDates.size() - 1)
                .mapToObj(i -> new DateRange(sortedDates.get(i), sortedDates.get(i + 1)))
                .map(interval -> interval.intersect(waterTariff.dateRange()))
                .flatMap(Optional::stream)
                .toList();
    }

    private static BigDecimal calculateTotalConsumption(
            List<ColdWaterReading> detailWaterReadings, DateRange interval) {
        return detailWaterReadings.stream()
                .map(
                        reading ->
                                reading.dateRange()
                                        .intersect(interval)
                                        .map(
                                                overlap -> {
                                                    BigDecimal monthlyCost =
                                                            reading.consumption()
                                                                    .divide(
                                                                            reading.dateRange()
                                                                                    .getMonthCount(),
                                                                            10,
                                                                            RoundingMode.HALF_UP);
                                                    return overlap.getMonthCount()
                                                            .multiply(monthlyCost)
                                                            .setScale(10, RoundingMode.HALF_UP);
                                                })
                                        .orElse(BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
