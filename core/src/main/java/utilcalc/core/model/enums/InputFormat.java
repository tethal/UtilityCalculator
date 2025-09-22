package utilcalc.core.model.enums;

public enum InputFormat {
    TOML("toml");

    private final String value;

    InputFormat(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
