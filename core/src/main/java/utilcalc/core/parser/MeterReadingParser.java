package utilcalc.core.parser;

import static utilcalc.core.parser.ParserUtils.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import org.tomlj.TomlArray;
import org.tomlj.TomlTable;
import utilcalc.core.model.input.MeterReading;

class MeterReadingParser {

    private MeterReadingParser() {}

    private static final String METER_ID = "meter_id";
    private static final String READING_DATE = "reading_date";
    private static final String STATE = "state";

    private static final Set<String> METER_READING_KNOWN_FIELDS =
            Set.of(METER_ID, READING_DATE, STATE);

    static List<MeterReading> parse(TomlArray meterReadings) {
        return IntStream.range(0, meterReadings.size())
                .mapToObj(meterReadings::getTable)
                .map(MeterReadingParser::parseMeterReading)
                .toList();
    }

    private static MeterReading parseMeterReading(TomlTable meterReadingTable) {
        checkThatSectionContainsOnlyKnownFields(
                meterReadingTable,
                METER_READING_KNOWN_FIELDS,
                ColdWaterSectionParser.READING_SECTION_NAME);
        String meterId = requireString(meterReadingTable, METER_ID);
        LocalDate readingDate = requireLocalDate(meterReadingTable, READING_DATE);
        BigDecimal state = requireBigDecimal(meterReadingTable, STATE);
        return new MeterReading(meterId, readingDate, state);
    }
}
