package utilcalc.web.report.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record HotWaterSectionInputsRequest(
        @NotBlank String name,
        @Valid @NotEmpty List<@NotNull MeterReadingRequest> readings,
        @Valid @NotEmpty List<@NotNull WaterTariffRequest> priceList,
        @Valid @NotEmpty List<@NotNull ServiceCostRequest> heatingBasicCosts,
        @Valid @NotEmpty List<@NotNull WaterTariffRequest> heatingConsumableTariffs) {

    public HotWaterSectionInputsRequest {
        readings = List.copyOf(readings);
        priceList = List.copyOf(priceList);
        heatingBasicCosts = List.copyOf(heatingBasicCosts);
        heatingConsumableTariffs = List.copyOf(heatingConsumableTariffs);
    }
}
