package utilcalc.web.report.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MeterReadingRequest(
        @NotBlank String meterId, @NotNull LocalDate readingDate, @NotNull BigDecimal state) {}
