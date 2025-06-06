package utilcalc.core.parser;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import utilcalc.core.model.input.ReportInputs;

class ParserTest {

    @Test
    void valid_input_should_return_valid_ReportInputs_class() {
        ReportInputs inputs = Parser.parse(ParserTestHelper.getTestCaseContent("valid"));

        assertThat(inputs.startDate()).isEqualTo(LocalDate.of(2024, 2, 15));
        assertThat(inputs.endDate()).isEqualTo(LocalDate.of(2024, 12, 31));
        assertThat(inputs.tenant()).containsExactly("Jméno nájemníka", "Adresa nemovitosti");
        assertThat(inputs.owner()).containsExactly("Jméno majitele", "majitel@example.com");
        assertThat(inputs.reportPlace()).isEqualTo("V Praze");
        assertThat(inputs.reportDate()).isEqualTo(LocalDate.of(2025, 5, 20));
        assertThat(inputs.sources()).containsExactly("vyúčtování SVJ za rok 2024");
    }

    @Test
    void invalid_syntax_should_throw_ParsingException() {
        assertThatThrownBy(() -> Parser.parse(ParserTestHelper.getTestCaseContent("bad_syntax")))
                .isInstanceOf(ParsingException.class)
                .hasMessageContaining("Syntax error: ");
    }

    @Test
    void unknown_field_should_throw_ParsingException() {
        assertThatThrownBy(
                        () ->
                                Parser.parse(
                                        ParserTestHelper.getTestCaseContent(
                                                "unknown_field_in_general_section")))
                .isInstanceOf(ParsingException.class)
                .hasMessage("Section general contains unknown fields: [unknown, second_unknown]");
    }

    @CsvSource({
        "missing_general_section,Missing required table field: general",
        "missing_start_date_general_section,Missing required date field: start_date",
        "missing_end_date_general_section,Missing required date field: end_date",
        "missing_tenant_general_section,Missing required array field: tenant",
        "missing_owner_general_section,Missing required array field: owner",
        "missing_report_place_general_section,Missing required string field: report_place",
        "missing_report_date_general_section,Missing required date field: report_date"
    })
    @ParameterizedTest
    void missing_required_field_should_throw_ParsingException(String textCase, String message) {
        assertThatThrownBy(() -> Parser.parse(ParserTestHelper.getTestCaseContent(textCase)))
                .isInstanceOf(ParsingException.class)
                .hasMessage(message);
    }

    @CsvSource({"missing_sources_general_section"})
    @ParameterizedTest
    void missing_optional_field_should_not_throw_ParsingException(String textCase) {
        assertThatNoException()
                .isThrownBy(() -> Parser.parse(ParserTestHelper.getTestCaseContent(textCase)));
    }

    @CsvSource({
        "wrong_data_type_start_date_general_section,Invalid data type: Value of 'start_date' is a integer",
        "wrong_data_type_end_date_general_section,Invalid data type: Value of 'end_date' is a integer",
        "wrong_data_type_tenant_general_section,Invalid data type: Value of 'tenant' is a local date",
        "wrong_data_type_owner_general_section,Invalid data type: Value of 'owner' is a local date",
        "wrong_data_type_report_place_general_section,Invalid data type: Value of 'report_place' is a local date",
        "wrong_data_type_report_date_general_section,Invalid data type: Value of 'report_date' is a string",
        "wrong_data_type_sources_general_section,Invalid data type: Value of 'sources' is a local date"
    })
    @ParameterizedTest
    void invalid_field_data_type_should_throw_ParsingException(String textCase, String message) {
        assertThatThrownBy(() -> Parser.parse(ParserTestHelper.getTestCaseContent(textCase)))
                .isInstanceOf(ParsingException.class)
                .hasMessage(message);
    }
}
