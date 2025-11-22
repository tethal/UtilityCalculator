package utilcalc.web.report.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record HeatingFeeInputsRequest(
        @NotBlank String name, @Valid @NotEmpty List<@NotNull ServiceCostRequest> heatingFees) {
    public HeatingFeeInputsRequest {
        heatingFees = List.copyOf(heatingFees);
    }
}
