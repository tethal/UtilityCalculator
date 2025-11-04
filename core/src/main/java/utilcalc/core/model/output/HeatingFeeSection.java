package utilcalc.core.model.output;

import static utilcalc.core.utils.Util.*;

import java.math.BigDecimal;
import java.util.List;

public record HeatingFeeSection(String name, BigDecimal totalAmount, List<HeatingFee> fees) implements ReportSection {
	public HeatingFeeSection {
		ensureNonBlank(name, "name");
		ensureNonNull(totalAmount, "totalAmount");
		ensureNonEmpty(fees, "fees");

		fees = List.copyOf(fees);
	}
}
