package utilcalc.web.report.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PaymentRequest(@NotBlank String description, @NotNull BigDecimal count, @NotNull BigDecimal amount) {}
