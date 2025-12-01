package utilcalc.core.reportGen;

public enum Format {
    PDF("pdf"),
    HTML("html"),
    TYPST("typ");

    private final String extension;

    Format(String extension) {
        this.extension = extension;
    }

    public static Format fromString(String value) {
        return Format.valueOf(value.toUpperCase());
    }

    public String getExtension() {
        return extension;
    }
}
