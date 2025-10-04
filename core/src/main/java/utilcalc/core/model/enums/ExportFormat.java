package utilcalc.core.model.enums;

public enum ExportFormat {
    PDF("pdf"),
    HTML("html");

    private final String value;

    ExportFormat(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ExportFormat fromString(String value) {
        for (ExportFormat format : ExportFormat.values()) {
            if (format.value.equalsIgnoreCase(value)) {
                return format;
            }
        }
        throw new IllegalArgumentException("Unsupported export format: " + value);
    }
}
