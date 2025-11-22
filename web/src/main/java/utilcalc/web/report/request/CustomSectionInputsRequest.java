package utilcalc.web.report.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CustomSectionInputsRequest(@NotBlank String name, @NotNull BigDecimal totalAmount) {}
