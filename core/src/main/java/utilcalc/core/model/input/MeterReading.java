package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.ensureNonBlank;
import static utilcalc.core.utils.Util.ensureNonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MeterReading(String meterId, LocalDate readingDate, BigDecimal state) {
	public MeterReading {
		ensureNonBlank(meterId, "meterId");
		ensureNonNull(readingDate, "readingDate");
		ensureNonNull(state, "state");
	}
}
