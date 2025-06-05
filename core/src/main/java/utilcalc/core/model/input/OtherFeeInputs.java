package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.ensureNonEmpty;

import java.util.List;

public final class OtherFeeInputs extends SectionInput {
    private final List<ServiceCost> otherFees;

    public OtherFeeInputs(String name, List<ServiceCost> otherFees) {
        super(name);

        ensureNonEmpty(otherFees, "otherFees");
        this.otherFees = List.copyOf(otherFees);
    }

    public List<ServiceCost> getOtherFees() {
        return otherFees;
    }
}
