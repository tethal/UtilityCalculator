package utilcalc.web.report.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record WaterTariffRequest(
        @NotNull LocalDate startDate, @NotNull LocalDate endDate, @NotNull BigDecimal pricePerCubicMeter) {}
