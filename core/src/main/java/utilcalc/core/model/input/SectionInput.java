package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.ensureNonBlank;

public abstract class SectionInput implements SectionInputs {
    protected final String name;

    protected SectionInput(String name) {
        ensureNonBlank(name, "name");
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
