package utilcalc.core.reportGen;

import utilcalc.core.model.input.CustomSectionInputs;
import utilcalc.core.model.output.CustomSection;

final class CustomSectionGenerator {

    private CustomSectionGenerator() {}

    static CustomSection generateCustomSection(CustomSectionInputs customSectionInputs) {
        return new CustomSection(customSectionInputs.name(), customSectionInputs.totalAmount());
    }
}
