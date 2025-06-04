package utilcalc.core.in;

import java.util.List;

public final class OtherFeeInputs extends SectionInput {
    private final List<ServiceCost> otherFees;

    public OtherFeeInputs(String name, List<ServiceCost> otherFees) {
        super(name);

        if (otherFees == null || otherFees.isEmpty()) {
            throw new IllegalArgumentException("otherFees is null or empty");
        }
        this.otherFees = List.copyOf(otherFees);
    }

    public List<ServiceCost> getOtherFees() {
        return otherFees;
    }
}
