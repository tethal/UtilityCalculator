package utilcalc.web.report.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record DepositsSectionInputsRequest(
        @NotBlank String name, @Valid @NotEmpty List<@NotNull PaymentRequest> payments) {
    public DepositsSectionInputsRequest {
        payments = List.copyOf(payments);
    }
}
