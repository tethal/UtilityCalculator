package utilcalc.core.parser.newparser;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.*;
import utilcalc.core.util.TestHelpers;

class ParserTest {

    @Test
    void valid_input_should_return_valid_ReportInputs_class() {
        ReportInputs inputs = Parser.parse(TestHelpers.getNewTestCaseContent("valid"));

        assertThat(inputs.dateRange().startDate()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(inputs.dateRange().endDateExclusive()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(inputs.tenant())
                .containsExactly("Marie Černá", "Jindřišská 16", "111 50 Praha 1");
        assertThat(inputs.owner()).containsExactly("Jan Novák", "majitel@example.com");
        assertThat(inputs.reportPlace()).isEqualTo("V Praze");
        assertThat(inputs.reportDate()).isEqualTo(LocalDate.of(2025, 5, 8));
        assertThat(inputs.sources())
                .containsExactly("Vyúčtování služeb od Společenství vlastníků za rok 2024");
        assertThat(inputs.sections()).hasSize(3);
        DepositsSectionInputs deposits = (DepositsSectionInputs) inputs.sections().getFirst();
        assertThat(deposits.name()).isEqualTo("Přijaté zálohy");
        assertThat(deposits.payments())
                .hasSize(1)
                .containsExactly(
                        new Payment(
                                "leden - prosinec",
                                BigDecimal.valueOf(12),
                                BigDecimal.valueOf(3000)));

        HeatingFeeInputs heating = (HeatingFeeInputs) inputs.sections().get(1);
        assertThat(heating.name()).isEqualTo("Vytápění");
        assertThat(heating.heatingFees())
                .hasSize(1)
                .containsExactly(
                        new ServiceCost(
                                new DateRange(LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1)),
                                BigDecimal.valueOf(8712.9)));

        OtherFeeInputs otherFee = (OtherFeeInputs) inputs.sections().get(2);
        assertThat(otherFee.name()).isEqualTo("Náklady");
        assertThat(otherFee.otherFees())
                .hasSize(1)
                .containsExactly(
                        new ServiceCost(
                                new DateRange(LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1)),
                                new BigDecimal("39514.30")));
    }
}
