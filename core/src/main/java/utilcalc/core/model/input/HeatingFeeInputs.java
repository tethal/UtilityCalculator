package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.ensureNonBlank;
import static utilcalc.core.utils.Util.ensureNonEmpty;

import java.util.List;

public record HeatingFeeInputs(String name, List<ServiceCost> heatingFees) implements SectionInputs {
	public HeatingFeeInputs {
		ensureNonBlank(name, "name");
		ensureNonEmpty(heatingFees, "heatingFees");
		heatingFees = List.copyOf(heatingFees);
	}
}
