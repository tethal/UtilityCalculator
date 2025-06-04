package utilcalc.core.in;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings(
        value = "CT_CONSTRUCTOR_THROW",
        justification =
                "Constructor validates input; class is abstract and safe from finalizer attacks")
public abstract class SectionInput implements SectionInputs {
    protected final String name;

    protected SectionInput(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name can not be null or empty");
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
