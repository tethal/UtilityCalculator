package utilcalc.core.parser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import utilcalc.core.model.input.MeterReading;

class MeterReadingParser {

    private MeterReadingParser() {}

    static List<MeterReading> parseLine(String line, String defaultMeterId) {
        // Syntax: 2024-01-01: SV1 @ 14.5, SV2 @ 55.6
        String[] parts = line.split(":", 2);
        if (parts.length != 2) {
            throw new ParsingException("Invalid meter reading line (missing ':'): " + line);
        }

        String header = parts[0].strip();
        LocalDate date = parseLocalDate(header);

        String readingsPart = parts[1].strip();
        String[] readingTokens = readingsPart.split(",");

        List<MeterReading> readings = new ArrayList<>();
        for (String token : readingTokens) {
            String[] idAndValue = token.strip().split("@");
            String meterId;
            BigDecimal state;

            if (idAndValue.length == 1) {
                meterId = defaultMeterId;
                state = ExprParser.parse(idAndValue[0].strip());
            } else if (idAndValue.length == 2) {
                meterId = idAndValue[0].strip();
                state = ExprParser.parse(idAndValue[1].strip());
            } else {
                throw new ParsingException("Invalid reading expression: " + token);
            }

            readings.add(new MeterReading(meterId, date, state));
        }

        return readings;
    }

    private static LocalDate parseLocalDate(String text) {
        return LocalDate.parse(text);
    }
}
