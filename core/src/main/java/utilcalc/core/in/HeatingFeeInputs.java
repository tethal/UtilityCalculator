package utilcalc.core.in;

import java.util.List;

public final class HeatingFeeInputs extends SectionInput {
    private final List<ServiceCost> heatingFees;

    public HeatingFeeInputs(String name, List<ServiceCost> heatingFees) {
        super(name);

        if (heatingFees == null || heatingFees.isEmpty()) {
            throw new IllegalArgumentException("heatingFees must not be null or empty");
        }

        this.heatingFees = List.copyOf(heatingFees);
    }

    public List<ServiceCost> getHeatingFees() {
        return heatingFees;
    }
}
