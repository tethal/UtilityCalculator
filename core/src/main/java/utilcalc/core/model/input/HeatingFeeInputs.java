package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.ensureNonEmpty;

import java.util.List;

public final class HeatingFeeInputs extends SectionInput {
    private final List<ServiceCost> heatingFees;

    public HeatingFeeInputs(String name, List<ServiceCost> heatingFees) {
        super(name);

        ensureNonEmpty(heatingFees, "heatingFees");
        this.heatingFees = List.copyOf(heatingFees);
    }

    public List<ServiceCost> getHeatingFees() {
        return heatingFees;
    }
}
