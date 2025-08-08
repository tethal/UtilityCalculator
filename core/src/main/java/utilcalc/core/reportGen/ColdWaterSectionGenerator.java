package utilcalc.core.reportGen;

import static utilcalc.core.reportGen.ReportGenUtil.validateDateRangeCoverage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.ColdWaterSectionInputs;
import utilcalc.core.model.input.MeterReading;
import utilcalc.core.model.input.WaterTariff;
import utilcalc.core.model.output.ColdWaterSection;
import utilcalc.core.model.output.WaterFee;
import utilcalc.core.model.output.WaterReading;

final class ColdWaterSectionGenerator {

    private ColdWaterSectionGenerator() {}

    static ColdWaterSection generateColdWaterSection(
            DateRange reportDateRange, ColdWaterSectionInputs coldWaterSectionInputs) {

        List<WaterTariff> waterTariffs = coldWaterSectionInputs.priceList();

        validateDateRangeCoverage(
                reportDateRange, waterTariffs, WaterTariff::dateRange, "WaterTariff");

        List<WaterReading> readings =
                coldWaterSectionInputs.readings().stream()
                        .collect(Collectors.groupingBy(MeterReading::meterId))
                        .values()
                        .stream()
                        .flatMap(group -> createColdWaterReadings(reportDateRange, group).stream())
                        .toList();

        List<WaterFee> priceList =
                waterTariffs.stream()
                        .sorted(Comparator.comparing(WaterTariff::dateRange))
                        .flatMap(waterTariff -> createColdWaterFees(waterTariff, readings).stream())
                        .toList();

        BigDecimal totalAmount =
                priceList.stream()
                        .map(WaterFee::periodAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ColdWaterSection(
                coldWaterSectionInputs.name(), totalAmount, readings, priceList);
    }

    // region Cold water readings
    private static List<WaterReading> createColdWaterReadings(
            DateRange reportDateRange, List<MeterReading> readings) {

        List<MeterReading> sortedMeterReadings =
                readings.stream()
                        .sorted(Comparator.comparing(MeterReading::readingDate))
                        .collect(Collectors.toList());

        List<WaterReading> waterReadings =
                createColdWaterReadingsFromMeterData(sortedMeterReadings);

        WaterReading firstReading = waterReadings.getFirst();
        WaterReading lastReading = waterReadings.getLast();

        if (reportDateRange.startDate().isBefore(firstReading.dateRange().startDate())) {
            waterReadings.addFirst(
                    createMissingColdWaterReading(reportDateRange, firstReading, true));
        }

        if (reportDateRange
                .endDateExclusive()
                .isAfter(lastReading.dateRange().endDateExclusive())) {
            waterReadings.addLast(
                    createMissingColdWaterReading(reportDateRange, lastReading, false));
        }

        return waterReadings;
    }

    private static WaterReading createMissingColdWaterReading(
            DateRange reportDateRange, WaterReading reading, boolean isStart) {

        String meterId = reading.meterId();
        BigDecimal startState = reading.startState();
        BigDecimal endState = reading.endState();
        DateRange readingDateRange = reading.dateRange();

        DateRange newDateRange =
                isStart
                        ? new DateRange(reportDateRange.startDate(), readingDateRange.startDate())
                        : new DateRange(
                                readingDateRange.endDateExclusive(),
                                reportDateRange.endDateExclusive());

        BigDecimal newConsumption =
                calculateConsumption(newDateRange, reading).setScale(3, RoundingMode.HALF_UP);

        BigDecimal newStartState =
                isStart
                        ? startState.subtract(newConsumption).setScale(3, RoundingMode.HALF_UP)
                        : endState;

        BigDecimal newEndState =
                isStart
                        ? startState
                        : endState.add(newConsumption).setScale(3, RoundingMode.HALF_UP);

        return new WaterReading(newDateRange, meterId, newStartState, newEndState, newConsumption);
    }

    private static List<WaterReading> createColdWaterReadingsFromMeterData(
            List<MeterReading> meterReadings) {
        return IntStream.range(0, meterReadings.size() - 1)
                .mapToObj(
                        i -> createColdWaterReading(meterReadings.get(i), meterReadings.get(i + 1)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static WaterReading createColdWaterReading(
            MeterReading currentReading, MeterReading nextReading) {
        BigDecimal startReadingState = currentReading.state();
        BigDecimal nextReadingState = nextReading.state();
        String meterId = currentReading.meterId();

        if (currentReading.readingDate().equals(nextReading.readingDate())) {
            return null;
        }

        DateRange range = new DateRange(currentReading.readingDate(), nextReading.readingDate());
        BigDecimal consumption = nextReadingState.subtract(startReadingState);
        return new WaterReading(range, meterId, startReadingState, nextReadingState, consumption);
    }

    // endregion

    // region Cold water fee

    private static List<WaterFee> createColdWaterFees(
            WaterTariff waterTariff, List<WaterReading> waterReadings) {
        return extractValidIntervals(waterTariff, waterReadings).stream()
                .map(
                        interval ->
                                createColdWaterFee(
                                        waterTariff.pricePerCubicMeter(), waterReadings, interval))
                .toList();
    }

    private static WaterFee createColdWaterFee(
            BigDecimal pricePerCubicMeter, List<WaterReading> waterReadings, DateRange interval) {

        BigDecimal quantity = calculateQuantity(waterReadings, interval);
        BigDecimal periodAmount =
                quantity.multiply(pricePerCubicMeter).setScale(2, RoundingMode.HALF_UP);

        return new WaterFee(
                interval,
                quantity.setScale(3, RoundingMode.HALF_UP),
                pricePerCubicMeter,
                periodAmount);
    }

    private static List<DateRange> extractValidIntervals(
            WaterTariff waterTariff, List<WaterReading> meterReadings) {

        Set<LocalDate> datePoints =
                meterReadings.stream()
                        .flatMap(reading -> reading.dateRange().stream())
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

    private static BigDecimal calculateQuantity(
            List<WaterReading> detailWaterReadings, DateRange interval) {
        return detailWaterReadings.stream()
                .map(
                        reading ->
                                reading.dateRange()
                                        .intersect(interval)
                                        .map(overlap -> calculateConsumption(overlap, reading))
                                        .orElse(BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // endregion

    private static BigDecimal calculateConsumption(DateRange dateRange, WaterReading reading) {
        BigDecimal readingConsumption = reading.consumption();
        BigDecimal monthCount = reading.dateRange().getMonthCount();
        BigDecimal monthlyConsumption =
                readingConsumption.divide(monthCount, 10, RoundingMode.HALF_UP);

        return monthlyConsumption
                .multiply(dateRange.getMonthCount())
                .setScale(10, RoundingMode.HALF_UP);
    }
}
