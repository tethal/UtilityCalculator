package utilcalc.web.report.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ColdWaterSectionInputsRequest(
        @NotBlank String name,
        @Valid @NotEmpty List<@NotNull MeterReadingRequest> readings,
        @Valid @NotEmpty List<@NotNull WaterTariffRequest> priceList) {
    public ColdWaterSectionInputsRequest {
        readings = List.copyOf(readings);
        priceList = List.copyOf(priceList);
    }
}
