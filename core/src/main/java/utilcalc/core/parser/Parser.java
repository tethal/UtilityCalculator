package utilcalc.core.parser;

import static utilcalc.core.parser.ParserUtil.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.ReportInputs;
import utilcalc.core.model.input.SectionInputs;

public class Parser {

    private static final String TENANT = "najemnik";
    private static final String OWNER = "najemce";
    private static final String SOURCES = "zdroje";

    private Parser() {}

    public static ReportInputs parse(String input) {
        LineReader reader = new LineReader(input);

        DateRange dateRange = parseRange(reader.nextLine());
        StringDatePair placeAndDate = parseStringDatePair(reader.nextLine());
        String reportPlace = placeAndDate.string();
        LocalDate reportDate = placeAndDate.date();

        List<String> tenant = new ArrayList<>();
        List<String> owner = new ArrayList<>();
        List<String> sources = new ArrayList<>();
        List<SectionInputs> sections = new ArrayList<>();

        while (reader.hasMore()) {
            GroupHeader header = parseGroupHeader(reader.nextLine());

            switch (header.name().toLowerCase()) {
                case TENANT -> tenant.addAll(reader.readAllUntilNextGroup());
                case OWNER -> owner.addAll(reader.readAllUntilNextGroup());
                case SOURCES -> sources.addAll(reader.readAllUntilNextGroup());
                case DepositsSectionParser.SECTION_NAME -> sections.add(
                        DepositsSectionParser.parse(header, reader.readAllUntilNextGroup()));
                case HeatingSectionParser.SECTION_NAME -> sections.add(
                        HeatingSectionParser.parse(header, reader.readAllUntilNextGroup()));
                case OtherFeesSectionParser.SECTION_NAME -> sections.add(
                        OtherFeesSectionParser.parse(header, reader.readAllUntilNextGroup()));
                case ColdWaterSectionParser.SECTION_NAME -> sections.add(
                        ColdWaterSectionParser.parse(header, reader.readAllUntilNextGroup()));
                case HotWaterSectionParser.SECTION_NAME -> sections.add(
                        HotWaterSectionParser.parse(header, reader.readAllUntilNextGroup()));
                default -> throw new ParsingException("Unknown group: " + header.name());
            }
        }

        return new ReportInputs(
                dateRange, tenant, owner, reportPlace, reportDate, sources, sections);
    }

    private static StringDatePair parseStringDatePair(String line) {
        String[] parts = line.split(",", 2);
        if (parts.length != 2) {
            throw new ParsingException("Expected two parts separated by ',', got: " + line);
        }
        return new StringDatePair(parts[0].strip(), LocalDate.parse(parts[1].strip()));
    }

    private record StringDatePair(String string, LocalDate date) {}
}
