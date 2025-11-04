package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.ensureNonNull;

import java.math.BigDecimal;
import utilcalc.core.model.DateRange;

public record WaterTariff(DateRange dateRange, BigDecimal pricePerCubicMeter) {
	public WaterTariff {
		ensureNonNull(dateRange, "dateRange");
		ensureNonNull(pricePerCubicMeter, "pricePerCubicMeter");
	}
}
