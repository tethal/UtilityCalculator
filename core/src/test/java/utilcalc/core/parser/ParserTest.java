package utilcalc.core.parser;

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
		assertThat(inputs.tenant()).containsExactly("Marie Černá", "Jindřišská 16", "111 50 Praha 1");
		assertThat(inputs.owner()).containsExactly("Jan Novák", "majitel@example.com");
		assertThat(inputs.reportPlace()).isEqualTo("V Praze");
		assertThat(inputs.reportDate()).isEqualTo(LocalDate.of(2025, 5, 8));
		assertThat(inputs.sources()).containsExactly("Vyúčtování služeb od Společenství vlastníků za rok 2024");

		assertThat(inputs.sections()).hasSize(5);

		DepositsSectionInputs deposits = (DepositsSectionInputs) inputs.sections().getFirst();
		assertThat(deposits.name()).isEqualTo("Přijaté zálohy");
		assertThat(deposits.payments()).hasSize(1)
				.containsExactly(new Payment("leden - prosinec", BigDecimal.valueOf(12), BigDecimal.valueOf(3000)));

		HeatingFeeInputs heating = (HeatingFeeInputs) inputs.sections().get(1);
		assertThat(heating.name()).isEqualTo("Vytápění");
		assertThat(heating.heatingFees()).hasSize(1).containsExactly(new ServiceCost(
				new DateRange(LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1)), BigDecimal.valueOf(8712.9)));

		OtherFeeInputs otherFee = (OtherFeeInputs) inputs.sections().get(2);
		assertThat(otherFee.name()).isEqualTo("Náklady");
		assertThat(otherFee.otherFees()).hasSize(1).containsExactly(new ServiceCost(
				new DateRange(LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1)), new BigDecimal("39514.30")));

		ColdWaterSectionInputs cold = (ColdWaterSectionInputs) inputs.sections().get(3);
		assertThat(cold.name()).isEqualTo("Studená voda");
		assertThat(cold.readings()).hasSize(2).containsExactly(
				new MeterReading("SV", LocalDate.of(2024, 1, 1), new BigDecimal("60.7")),
				new MeterReading("SV", LocalDate.of(2025, 1, 1), new BigDecimal("102.9")));
		assertThat(cold.priceList()).hasSize(1)
				.containsExactly(new WaterTariff(new DateRange(LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1)),
						new BigDecimal("140.2881516587678")));

		HotWaterSectionInputs hot = (HotWaterSectionInputs) inputs.sections().get(4);
		assertThat(hot.name()).isEqualTo("Teplá voda");

		assertThat(hot.readings()).hasSize(2).containsExactly(
				new MeterReading("TV", LocalDate.of(2024, 1, 1), new BigDecimal("30.7")),
				new MeterReading("TV", LocalDate.of(2025, 1, 1), new BigDecimal("51.9")));

		assertThat(hot.priceList()).hasSize(1)
				.containsExactly(new WaterTariff(new DateRange(LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1)),
						new BigDecimal("122.8509433962264")));

		assertThat(hot.heatingBasicCosts()).hasSize(1).containsExactly(new ServiceCost(
				new DateRange(LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1)), new BigDecimal("2725.92")));

		assertThat(hot.heatingConsumableTariffs()).hasSize(1).containsExactly(new WaterTariff(
				new DateRange(LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1)), new BigDecimal("257.988")));
	}

	@Test
	void real_2021_should_return_valid_ReportInputs_class() {
		ReportInputs inputs = Parser.parse(TestHelpers.getNewTestCaseContent("real_2021"));

		assertThat(inputs.dateRange().startDate()).isEqualTo(LocalDate.of(2021, 1, 1));
		assertThat(inputs.dateRange().endDateExclusive()).isEqualTo(LocalDate.of(2022, 4, 30).plusDays(1));
		assertThat(inputs.tenant()).containsExactly("Marie Černá", "Jindřišská 16", "111 50 Praha 1");
		assertThat(inputs.owner()).containsExactly("Jan Novák", "majitel@example.com");
		assertThat(inputs.reportPlace()).isEqualTo("V Praze");
		assertThat(inputs.reportDate()).isEqualTo(LocalDate.of(2022, 5, 22));
		assertThat(inputs.sources()).containsExactly("Vyúčtování služeb od Společenství vlastníků za rok 2021",
				"Příloha č. 3 k vyhlášce č.269/2015 Sb.",
				"Měsíční předpis záloh pro rok 2022 od Společenství vlastníků",
				"https://www.pvk.cz/vse-o-vode/cena-vodneho-a-stocneho/vyvoj-vodneho-a-stocneho-v-praze/");

		assertThat(inputs.sections()).hasSize(5);

		DepositsSectionInputs deposits = (DepositsSectionInputs) inputs.sections().getFirst();
		assertThat(deposits.name()).isEqualTo("Přijaté zálohy");
		assertThat(deposits.payments()).hasSize(1)
				.containsExactly(new Payment("celkem", BigDecimal.ONE, BigDecimal.valueOf(43500)));

		HeatingFeeInputs heating = (HeatingFeeInputs) inputs.sections().get(1);
		assertThat(heating.name()).isEqualTo("Vytápění");
		assertThat(heating.heatingFees()).hasSize(2).containsExactly(
				new ServiceCost(new DateRange(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1)),
						BigDecimal.valueOf(8712.9)),
				new ServiceCost(new DateRange(LocalDate.of(2022, 1, 1), LocalDate.of(2023, 1, 1)),
						BigDecimal.valueOf(8472)));

		OtherFeeInputs otherFee = (OtherFeeInputs) inputs.sections().get(2);
		assertThat(otherFee.name()).isEqualTo("Ostatní poplatky");
		assertThat(otherFee.otherFees()).hasSize(2).containsExactly(
				new ServiceCost(new DateRange(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1)),
						BigDecimal.valueOf(8772)),
				new ServiceCost(new DateRange(LocalDate.of(2022, 1, 1), LocalDate.of(2023, 1, 1)),
						BigDecimal.valueOf(8508)));

		ColdWaterSectionInputs cold = (ColdWaterSectionInputs) inputs.sections().get(3);
		assertThat(cold.name()).isEqualTo("Studená voda");
		assertThat(cold.readings()).hasSize(5).containsExactly(
				new MeterReading("SV", LocalDate.of(2021, 1, 1), new BigDecimal("158.1")),
				new MeterReading("SV", LocalDate.of(2021, 10, 15), new BigDecimal("184.4")),
				new MeterReading("SV", LocalDate.of(2021, 10, 15), new BigDecimal("0")),
				new MeterReading("SV", LocalDate.of(2022, 1, 1), new BigDecimal("8.6")),
				new MeterReading("SV", LocalDate.of(2022, 5, 1), new BigDecimal("21")));
		assertThat(cold.priceList()).hasSize(2).containsExactly(
				new WaterTariff(new DateRange(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1)),
						new BigDecimal("103.4076")),
				new WaterTariff(new DateRange(LocalDate.of(2022, 1, 1), LocalDate.of(2023, 1, 1)),
						new BigDecimal("108.13")));

		HotWaterSectionInputs hot = (HotWaterSectionInputs) inputs.sections().get(4);
		assertThat(hot.name()).isEqualTo("Teplá voda");

		assertThat(hot.readings()).hasSize(5).containsExactly(
				new MeterReading("TV", LocalDate.of(2021, 1, 1), new BigDecimal("96.6")),
				new MeterReading("TV", LocalDate.of(2021, 10, 15), new BigDecimal("106.5")),
				new MeterReading("TV", LocalDate.of(2021, 10, 15), new BigDecimal("0")),
				new MeterReading("TV", LocalDate.of(2022, 1, 1), new BigDecimal("5.5")),
				new MeterReading("TV", LocalDate.of(2022, 5, 1), new BigDecimal("13.3")));

		assertThat(hot.priceList()).hasSize(2).containsExactly(
				new WaterTariff(new DateRange(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1)),
						new BigDecimal("81.49536")),
				new WaterTariff(new DateRange(LocalDate.of(2022, 1, 1), LocalDate.of(2023, 1, 1)),
						new BigDecimal("108.13")));

		assertThat(hot.heatingBasicCosts()).hasSize(1).containsExactly(new ServiceCost(
				new DateRange(LocalDate.of(2021, 1, 1), LocalDate.of(2023, 1, 1)), new BigDecimal("1769.52")));

		assertThat(hot.heatingConsumableTariffs()).hasSize(1).containsExactly(new WaterTariff(
				new DateRange(LocalDate.of(2021, 1, 1), LocalDate.of(2023, 1, 1)), new BigDecimal("150.998035")));
	}
}
