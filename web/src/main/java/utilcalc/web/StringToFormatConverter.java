package utilcalc.web;

import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import utilcalc.core.reportGen.Format;

@Component
public class StringToFormatConverter implements Converter<@NonNull String, @NonNull Format> {

    @Override
    public @NonNull Format convert(@NonNull String source) {
        return Format.valueOf(source.trim().toUpperCase());
    }
}
