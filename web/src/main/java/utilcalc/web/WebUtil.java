package utilcalc.web;

import org.springframework.http.MediaType;
import utilcalc.core.reportGen.Format;

public final class WebUtil {

    public static MediaType mediaTypeFor(Format format) {
        return switch (format) {
            case PDF -> MediaType.APPLICATION_PDF;
            case HTML -> MediaType.TEXT_HTML;
            case TYPEST -> MediaType.valueOf("application/typst");
        };
    }

    public static String extensionFor(Format format) {
        return switch (format) {
            case PDF -> "pdf";
            case HTML -> "html";
            case TYPEST -> "typ";
        };
    }

    public static String sanitizeFilename(String input) {
        return input.replaceAll("[\\\\/\\r\\n\\t]", "") // odstraní slashes a control char
                .replaceAll("^\\.+", "") // žádné tečky na začátku
                .trim();
    }
}
