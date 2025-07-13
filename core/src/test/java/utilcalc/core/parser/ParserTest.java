package utilcalc.core.parser;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.*;

class ParserTest {

    @Test
    void valid_input_should_return_valid_ReportInputs_class() {
        ReportInputs inputs = Parser.parse(ParserTestHelper.getTestCaseContent("valid"));

        assertThat(inputs.dateRange().startDate()).isEqualTo(LocalDate.of(2024, 2, 15));
        assertThat(inputs.dateRange().endDateExclusive()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(inputs.tenant()).containsExactly("Jméno nájemníka", "Adresa nemovitosti");
        assertThat(inputs.owner()).containsExactly("Jméno majitele", "majitel@example.com");
        assertThat(inputs.reportPlace()).isEqualTo("V Praze");
        assertThat(inputs.reportDate()).isEqualTo(LocalDate.of(2025, 5, 20));
        assertThat(inputs.sources()).containsExactly("vyúčtování SVJ za rok 2024");
        assertThat(inputs.sections()).hasSize(4);

        DepositsSectionInputs deposits = (DepositsSectionInputs) inputs.sections().getFirst();
        assertThat(deposits.name()).isEqualTo("Přijaté zálohy");
        assertThat(deposits.payments())
                .hasSize(3)
                .containsExactly(
                        new Payment(
                                "leden - duben", BigDecimal.valueOf(4), BigDecimal.valueOf(3000)),
                        new Payment("květen", BigDecimal.valueOf(1), BigDecimal.valueOf(3500)),
                        new Payment(
                                "červen - prosinec",
                                BigDecimal.valueOf(7),
                                BigDecimal.valueOf(3500)));

        HeatingFeeInputs heating = (HeatingFeeInputs) inputs.sections().get(1);
        assertThat(heating.name()).isEqualTo("Vytápění");
        assertThat(heating.heatingFees())
                .hasSize(2)
                .containsExactly(
                        new ServiceCost(
                                new DateRange(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1)),
                                BigDecimal.valueOf(10992)),
                        new ServiceCost(
                                new DateRange(LocalDate.of(2022, 1, 1), LocalDate.of(2023, 1, 1)),
                                BigDecimal.valueOf(10992)));

        OtherFeeInputs otherFee = (OtherFeeInputs) inputs.sections().get(2);
        assertThat(otherFee.name()).isEqualTo("Ostatní poplatky");
        assertThat(otherFee.otherFees())
                .hasSize(2)
                .containsExactly(
                        new ServiceCost(
                                new DateRange(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1)),
                                BigDecimal.valueOf(3000)),
                        new ServiceCost(
                                new DateRange(LocalDate.of(2022, 1, 1), LocalDate.of(2023, 1, 1)),
                                BigDecimal.valueOf(3200)));

        ColdWaterSectionInputs coldWater = (ColdWaterSectionInputs) inputs.sections().get(3);
        assertThat(coldWater.name()).isEqualTo("Studená voda");
        assertThat(coldWater.readings())
                .hasSize(3)
                .containsExactly(
                        new MeterReading(
                                "1", LocalDate.of(2021, 10, 14), BigDecimal.valueOf(184.4)),
                        new MeterReading("2", LocalDate.of(2021, 12, 31), BigDecimal.valueOf(8.6)),
                        new MeterReading("2", LocalDate.of(2022, 4, 30), BigDecimal.valueOf(21)));
        assertThat(coldWater.priceList())
                .hasSize(2)
                .containsExactly(
                        new WaterTariff(
                                new DateRange(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1)),
                                BigDecimal.valueOf(103.4076)),
                        new WaterTariff(
                                new DateRange(LocalDate.of(2022, 1, 1), LocalDate.of(2023, 1, 1)),
                                BigDecimal.valueOf(108.13)));
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
        "missing_report_date_general_section,Missing required date field: report_date",
        "missing_description_deposits_section,Missing required string field: description",
        "missing_amount_deposits_section,Missing required bigDecimal field: amount",
        "missing_annual_cost_heating_section,Missing required bigDecimal field: annual_cost",
        "missing_start_date_heating_section,Missing required date field: start_date",
        "missing_end_date_heating_section,Missing required date field: end_date",
        "missing_annual_cost_other_fees_section,Missing required bigDecimal field: annual_cost",
        "missing_start_date_other_fees_section,Missing required date field: start_date",
        "missing_end_date_other_fees_section,Missing required date field: end_date",
        "missing_meter_id_cold_water_reading_section,Missing required string field: meter_id",
        "missing_reading_date_cold_water_reading_section,Missing required date field: reading_date",
        "missing_state_cold_water_reading_section,Missing required bigDecimal field: state",
        "missing_start_date_cold_water_tariff_section,Missing required date field: start_date",
        "missing_end_date_cold_water_tariff_section,Missing required date field: end_date",
        "missing_unit_amount_cold_water_tariff_section,Missing required bigDecimal field: unit_amount",
        "missing_tariff_array_cold_water_section,Missing required array field: tariff",
        "missing_reading_array_cold_water_section,Missing required array field: reading"
    })
    @ParameterizedTest
    void missing_required_field_should_throw_ParsingException(String textCase, String message) {
        assertThatThrownBy(() -> Parser.parse(ParserTestHelper.getTestCaseContent(textCase)))
                .isInstanceOf(ParsingException.class)
                .hasMessage(message);
    }

    @CsvSource({"missing_deposits_section"})
    @ParameterizedTest
    void missing_optional_section_should_not_throw_ParsingException(String textCase) {
        assertThatNoException()
                .isThrownBy(() -> Parser.parse(ParserTestHelper.getTestCaseContent(textCase)));
    }

    @CsvSource({"missing_sources_general_section", "missing_count_deposits_section"})
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
        "wrong_data_type_sources_general_section,Invalid data type: Value of 'sources' is a local date",
        "wrong_data_type_amount_deposits_section,Invalid data type: Value of 'amount' is a string",
        "wrong_data_type_count_deposits_section,Invalid data type: Value of 'count' is a string",
        "wrong_data_type_description_deposits_section,Invalid data type: Value of 'description' is a integer",
        "wrong_data_type_annual_cost_heating_section,Invalid data type: Value of 'annual_cost' is a string",
        "wrong_data_type_start_date_heating_section,Invalid data type: Value of 'start_date' is a string",
        "wrong_data_type_end_date_heating_section,Invalid data type: Value of 'end_date' is a string",
        "wrong_data_type_annual_cost_other_fees_section,Invalid data type: Value of 'annual_cost' is a string",
        "wrong_data_type_start_date_other_fees_section,Invalid data type: Value of 'start_date' is a string",
        "wrong_data_type_end_date_other_fees_section,Invalid data type: Value of 'end_date' is a string",
        "wrong_data_type_meter_id_cold_water_reading_section,Invalid data type: Value of 'meter_id' is a integer",
        "wrong_data_type_reading_date_cold_water_reading_section,Invalid data type: Value of 'reading_date' is a integer",
        "wrong_data_type_state_cold_water_reading_section,Invalid data type: Value of 'state' is a local date",
        "wrong_data_type_start_date_cold_water_tariff_section,Invalid data type: Value of 'start_date' is a integer",
        "wrong_data_type_end_date_cold_water_tariff_section,Invalid data type: Value of 'end_date' is a string",
        "wrong_data_type_unit_amount_cold_water_tariff_section,Invalid data type: Value of 'unit_amount' is a local date"
    })
    @ParameterizedTest
    void invalid_field_data_type_should_throw_ParsingException(String textCase, String message) {
        assertThatThrownBy(() -> Parser.parse(ParserTestHelper.getTestCaseContent(textCase)))
                .isInstanceOf(ParsingException.class)
                .hasMessage(message);
    }
}
