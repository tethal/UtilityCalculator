package utilcalc.web.report;

import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.*;
import utilcalc.web.report.request.*;

@Component
public class RequestToReportInputs implements Converter<@NonNull ReportInputsRequest, @NonNull ReportInputs> {

    @Override
    public @NonNull ReportInputs convert(ReportInputsRequest source) {
        DateRange dateRange = DateRange.fromInclusive(source.startDate(), source.endDate());

        List<SectionInputs> sections = new ArrayList<>();

        sections.addAll(source.deposits().stream()
                .map(RequestToReportInputs::requestToDepositsSection)
                .toList());

        sections.addAll(source.heating().stream()
                .map(RequestToReportInputs::requestToHeatingFee)
                .toList());

        sections.addAll(source.otherFees().stream()
                .map(RequestToReportInputs::requestToOtherFee)
                .toList());

        sections.addAll(source.coldWater().stream()
                .map(RequestToReportInputs::requestToColdWater)
                .toList());

        sections.addAll(source.hotWater().stream()
                .map(RequestToReportInputs::requestToHotWater)
                .toList());

        sections.addAll(source.custom().stream()
                .map(RequestToReportInputs::requestToCustom)
                .toList());

        return new ReportInputs(
                dateRange,
                source.tenant(),
                source.owner(),
                source.reportPlace(),
                source.reportDate(),
                source.sources(),
                List.copyOf(sections));
    }

    private static CustomSectionInputs requestToCustom(CustomSectionInputsRequest request) {
        return new CustomSectionInputs(request.name(), request.totalAmount());
    }

    private static HotWaterSectionInputs requestToHotWater(HotWaterSectionInputsRequest request) {
        return new HotWaterSectionInputs(
                request.name(),
                request.readings().stream()
                        .map(RequestToReportInputs::requestToMeterReading)
                        .toList(),
                request.priceList().stream()
                        .map(RequestToReportInputs::requestToWaterTariff)
                        .toList(),
                request.heatingBasicCosts().stream()
                        .map(RequestToReportInputs::requestToServiceCost)
                        .toList(),
                request.heatingConsumableTariffs().stream()
                        .map(RequestToReportInputs::requestToWaterTariff)
                        .toList());
    }

    private static ColdWaterSectionInputs requestToColdWater(ColdWaterSectionInputsRequest cw) {
        return new ColdWaterSectionInputs(
                cw.name(),
                cw.readings().stream()
                        .map(RequestToReportInputs::requestToMeterReading)
                        .toList(),
                cw.priceList().stream()
                        .map(RequestToReportInputs::requestToWaterTariff)
                        .toList());
    }

    private static WaterTariff requestToWaterTariff(WaterTariffRequest request) {
        return new WaterTariff(
                DateRange.fromInclusive(request.startDate(), request.endDate()), request.pricePerCubicMeter());
    }

    private static MeterReading requestToMeterReading(MeterReadingRequest request) {
        return new MeterReading(request.meterId(), request.readingDate(), request.state());
    }

    private static OtherFeeInputs requestToOtherFee(OtherFeeInputsRequest request) {
        return new OtherFeeInputs(
                request.name(),
                request.otherFees().stream()
                        .map(RequestToReportInputs::requestToServiceCost)
                        .toList());
    }

    private static HeatingFeeInputs requestToHeatingFee(HeatingFeeInputsRequest request) {
        return new HeatingFeeInputs(
                request.name(),
                request.heatingFees().stream()
                        .map(RequestToReportInputs::requestToServiceCost)
                        .toList());
    }

    private static ServiceCost requestToServiceCost(ServiceCostRequest request) {
        return new ServiceCost(DateRange.fromInclusive(request.startDate(), request.endDate()), request.annualCost());
    }

    private static DepositsSectionInputs requestToDepositsSection(DepositsSectionInputsRequest request) {
        return new DepositsSectionInputs(
                request.name(),
                request.payments().stream()
                        .map(RequestToReportInputs::requestToPayment)
                        .toList());
    }

    private static Payment requestToPayment(PaymentRequest request) {
        return new Payment(request.description(), request.count(), request.amount());
    }
}
