package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.ensureNonBlank;
import static utilcalc.core.utils.Util.ensureNonEmpty;

import java.util.List;

public record OtherFeeInputs(String name, List<ServiceCost> otherFees) implements SectionInputs {
	public OtherFeeInputs {
		ensureNonBlank(name, "name");
		ensureNonEmpty(otherFees, "otherFees");
		otherFees = List.copyOf(otherFees);
	}
}
