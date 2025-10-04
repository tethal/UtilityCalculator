package utilcalc.core.parser.newparser;

import static utilcalc.core.parser.newparser.ParserUtil.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.ReportInputs;
import utilcalc.core.model.input.SectionInputs;
import utilcalc.core.parser.ParsingException;

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

                    // TODO ostatní sekce

                default -> throw new ParsingException("Unknown group: " + header.name());
            }
        }

        return new ReportInputs(
                dateRange, tenant, owner, reportPlace, reportDate, sources, sections);
    }
}
