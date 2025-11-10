package utilcalc.core.reportGen;

import static utilcalc.core.reportGen.ReportGenUtil.validateDateRangeCoverage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.MeterReading;
import utilcalc.core.model.input.WaterTariff;
import utilcalc.core.model.output.WaterFee;
import utilcalc.core.model.output.WaterReading;

final class WaterSectionUtil {

    private WaterSectionUtil() {}

    // region water readings

    public static List<WaterReading> generateWaterReadings(
            DateRange reportDateRange, List<MeterReading> meterReadings) {
        return meterReadings.stream().collect(Collectors.groupingBy(MeterReading::meterId)).values().stream()
                .flatMap(group -> createColdWaterReadings(reportDateRange, group).stream())
                .toList();
    }

    private static List<WaterReading> createColdWaterReadings(DateRange reportDateRange, List<MeterReading> readings) {

        List<MeterReading> sortedMeterReadings = readings.stream()
                .sorted(Comparator.comparing(MeterReading::readingDate))
                .collect(Collectors.toList());

        LinkedList<WaterReading> waterReadings =
                new LinkedList<>(createColdWaterReadingsFromMeterData(sortedMeterReadings));

        LocalDate firstReadingStart = waterReadings.getFirst().dateRange().startDate();
        LocalDate lastReadingEnd = waterReadings.getLast().dateRange().endDateExclusive();

        LocalDate reportStart = reportDateRange.startDate();
        LocalDate reportEnd = reportDateRange.endDateExclusive();

        if (reportStart.isBefore(firstReadingStart)) addMissingStartWaterReading(reportDateRange, waterReadings, true);

        if (reportStart.isAfter(firstReadingStart)) addMissingStartWaterReading(reportDateRange, waterReadings, false);

        if (reportEnd.isBefore(lastReadingEnd)) addMissingEndWaterReading(reportDateRange, waterReadings, false);

        if (reportEnd.isAfter(lastReadingEnd)) addMissingEndWaterReading(reportDateRange, waterReadings, true);

        return waterReadings;
    }

    private static void addMissingStartWaterReading(
            DateRange reportDateRange, List<WaterReading> readings, boolean isInnerReading) {
        WaterReading first = readings.getFirst();
        WaterReading second = readings.get(1);
        LocalDate endDateExclusive = isInnerReading
                ? first.dateRange().startDate()
                : second.dateRange().startDate();
        BigDecimal startState = isInnerReading ? first.startState() : second.startState();

        DateRange newDateRange = new DateRange(reportDateRange.startDate(), endDateExclusive);
        BigDecimal newConsumption = calculateConsumption(newDateRange, first).setScale(3, RoundingMode.HALF_UP);
        BigDecimal newStartState = startState.subtract(newConsumption).setScale(3, RoundingMode.HALF_UP);
        WaterReading newWaterReading =
                new WaterReading(newDateRange, first.meterId(), newStartState, startState, newConsumption);

        if (!isInnerReading) readings.removeFirst();
        readings.addFirst(newWaterReading);
    }

    private static void addMissingEndWaterReading(
            DateRange reportDateRange, List<WaterReading> readings, boolean isInnerReading) {
        WaterReading last = readings.getLast();
        WaterReading penultimate = readings.size() > 1 ? readings.get(readings.size() - 2) : last;
        LocalDate startDate = isInnerReading
                ? last.dateRange().endDateExclusive()
                : penultimate.dateRange().endDateExclusive();
        BigDecimal endState = isInnerReading ? last.endState() : penultimate.endState();

        DateRange newDateRange = new DateRange(startDate, reportDateRange.endDateExclusive());
        BigDecimal newConsumption = calculateConsumption(newDateRange, last).setScale(3, RoundingMode.HALF_UP);
        BigDecimal newEndState = endState.add(newConsumption).setScale(3, RoundingMode.HALF_UP);
        WaterReading newWaterReading =
                new WaterReading(newDateRange, last.meterId(), endState, newEndState, newConsumption);
        if (!isInnerReading) readings.removeLast();
        readings.addLast(newWaterReading);
    }

    private static List<WaterReading> createColdWaterReadingsFromMeterData(List<MeterReading> meterReadings) {
        return IntStream.range(0, meterReadings.size() - 1)
                .mapToObj(i -> calculateColdWaterReading(meterReadings.get(i), meterReadings.get(i + 1)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static WaterReading calculateColdWaterReading(MeterReading currentReading, MeterReading nextReading) {
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

    // region water price list

    public static List<WaterFee> generatePriceList(
            DateRange reportDateRange, List<WaterTariff> waterTariffs, List<WaterReading> waterReadings) {

        validateDateRangeCoverage(reportDateRange, waterTariffs, WaterTariff::dateRange, "WaterTariff");

        return waterTariffs.stream()
                .sorted(Comparator.comparing(WaterTariff::dateRange))
                .flatMap(waterTariff -> createColdWaterFees(waterTariff, waterReadings).stream())
                .toList();
    }

    private static List<WaterFee> createColdWaterFees(WaterTariff waterTariff, List<WaterReading> waterReadings) {
        return extractValidIntervals(waterTariff, waterReadings).stream()
                .map(interval -> createColdWaterFee(waterTariff.pricePerCubicMeter(), waterReadings, interval))
                .toList();
    }

    private static WaterFee createColdWaterFee(
            BigDecimal pricePerCubicMeter, List<WaterReading> waterReadings, DateRange interval) {

        BigDecimal quantity = calculateQuantity(waterReadings, interval);
        BigDecimal periodAmount = quantity.multiply(pricePerCubicMeter).setScale(2, RoundingMode.HALF_UP);

        return new WaterFee(interval, quantity.setScale(3, RoundingMode.HALF_UP), pricePerCubicMeter, periodAmount);
    }

    private static List<DateRange> extractValidIntervals(WaterTariff waterTariff, List<WaterReading> meterReadings) {

        LocalDate startDateFirstReading = meterReadings.getFirst().dateRange().startDate();
        LocalDate endDateLastReading = meterReadings.getLast().dateRange().endDateExclusive();

        LocalDate waterTariffStartDate = waterTariff.dateRange().startDate();
        LocalDate waterTariffEndDate = waterTariff.dateRange().endDateExclusive();

        Set<LocalDate> datePoints = meterReadings.stream()
                .flatMap(reading -> reading.dateRange().stream())
                .collect(Collectors.toSet());

        if (waterTariffStartDate.isAfter(startDateFirstReading)) {
            datePoints.add(waterTariffStartDate);
        }

        if (waterTariffEndDate.isBefore(endDateLastReading)) {
            datePoints.add(waterTariffEndDate);
        }

        List<LocalDate> sortedDates = datePoints.stream().sorted().toList();

        return IntStream.range(0, sortedDates.size() - 1)
                .mapToObj(i -> new DateRange(sortedDates.get(i), sortedDates.get(i + 1)))
                .map(interval -> interval.intersect(waterTariff.dateRange()))
                .flatMap(Optional::stream)
                .toList();
    }

    private static BigDecimal calculateQuantity(List<WaterReading> detailWaterReadings, DateRange interval) {
        return detailWaterReadings.stream()
                .map(reading -> reading.dateRange()
                        .intersect(interval)
                        .map(overlap -> calculateConsumption(overlap, reading))
                        .orElse(BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // endregion

    private static BigDecimal calculateConsumption(DateRange dateRange, WaterReading reading) {
        BigDecimal readingConsumption = reading.consumption();
        BigDecimal monthCount = reading.dateRange().getMonthCount();
        BigDecimal monthlyConsumption = readingConsumption.divide(monthCount, 10, RoundingMode.HALF_UP);

        return monthlyConsumption.multiply(dateRange.getMonthCount()).setScale(10, RoundingMode.HALF_UP);
    }
}
