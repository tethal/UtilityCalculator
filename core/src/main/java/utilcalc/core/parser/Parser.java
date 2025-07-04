package utilcalc.core.parser;

import static utilcalc.core.parser.ParserUtils.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.tomlj.*;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.ReportInputs;
import utilcalc.core.model.input.SectionInputs;

public final class Parser {

    static final String GENERAL_SECTION_NAME = "general";

    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";
    private static final String TENANT = "tenant";
    private static final String OWNER = "owner";
    private static final String REPORT_PLACE = "report_place";
    private static final String REPORT_DATE = "report_date";
    private static final String SOURCES = "sources";

    private static final Set<String> GENERAL_SECTION_KNOWN_FIELDS =
            Set.of(START_DATE, END_DATE, TENANT, OWNER, REPORT_PLACE, REPORT_DATE, SOURCES);

    private Parser() {}

    public static ReportInputs parse(String input) {
        TomlParseResult parseResult = Toml.parse(input);
        if (parseResult.hasErrors()) {
            String errorMessages =
                    parseResult.errors().stream()
                            .map(TomlParseError::toString)
                            .collect(Collectors.joining("; "));
            throw new ParsingException("Syntax error: " + errorMessages);
        }

        try {
            return parseReportInputs(parseResult);
        } catch (TomlInvalidTypeException e) {
            throw new ParsingException("Invalid data type: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new ParsingException("Unknown error when parsing: " + e.getMessage(), e);
        }
    }

    private static ReportInputs parseReportInputs(TomlParseResult parseResult) {
        TomlTable general = requireTable(parseResult, GENERAL_SECTION_NAME);

        checkThatSectionContainsOnlyKnownFields(
                general, GENERAL_SECTION_KNOWN_FIELDS, GENERAL_SECTION_NAME);

        DateRange dateRange = requireDateRange(general, START_DATE, END_DATE);
        List<String> tenant = requireList(general, TENANT, String.class);
        List<String> owner = requireList(general, OWNER, String.class);
        String reportPlace = requireString(general, REPORT_PLACE);
        LocalDate reportDate = requireLocalDate(general, REPORT_DATE);
        List<String> sources = optionalList(general, SOURCES, String.class);

        List<SectionInputs> sections = SectionInputsParser.parse(parseResult);

        return new ReportInputs(
                dateRange, tenant, owner, reportPlace, reportDate, sources, sections);
    }
}
