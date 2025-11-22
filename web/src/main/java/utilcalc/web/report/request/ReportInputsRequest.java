package utilcalc.web.report.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record ReportInputsRequest(
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotEmpty List<@NotNull String> tenant,
        @NotEmpty List<@NotNull String> owner,
        @NotBlank String reportPlace,
        @NotNull LocalDate reportDate,
        @NotNull List<String> sources,
        @Valid @NotNull List<@NotNull DepositsSectionInputsRequest> deposits,
        @Valid @NotNull List<@NotNull HeatingFeeInputsRequest> heating,
        @Valid @NotNull List<@NotNull OtherFeeInputsRequest> otherFees,
        @Valid @NotNull List<@NotNull ColdWaterSectionInputsRequest> coldWater,
        @Valid @NotNull List<@NotNull HotWaterSectionInputsRequest> hotWater,
        @Valid @NotNull List<@NotNull CustomSectionInputsRequest> custom) {

    public ReportInputsRequest {

        tenant = List.copyOf(tenant);
        owner = List.copyOf(owner);
        sources = sources != null ? List.copyOf(sources) : List.of();
        deposits = deposits != null ? List.copyOf(deposits) : List.of();
        heating = heating != null ? List.copyOf(heating) : List.of();
        otherFees = otherFees != null ? List.copyOf(otherFees) : List.of();
        coldWater = coldWater != null ? List.copyOf(coldWater) : List.of();
        hotWater = hotWater != null ? List.copyOf(hotWater) : List.of();
        custom = custom != null ? List.copyOf(custom) : List.of();
    }
}
