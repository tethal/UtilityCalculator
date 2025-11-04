package utilcalc.core.reportGen;

import static utilcalc.core.reportGen.ReportGenUtil.calculateAmount;
import static utilcalc.core.reportGen.WaterSectionUtil.generatePriceList;
import static utilcalc.core.reportGen.WaterSectionUtil.generateWaterReadings;

import java.math.BigDecimal;
import java.util.*;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.ColdWaterSectionInputs;
import utilcalc.core.model.output.ColdWaterSection;
import utilcalc.core.model.output.WaterFee;
import utilcalc.core.model.output.WaterReading;

final class ColdWaterSectionGenerator {

	private ColdWaterSectionGenerator() {
	}

	static ColdWaterSection generateColdWaterSection(DateRange reportDateRange,
			ColdWaterSectionInputs coldWaterSectionInputs) {

		List<WaterReading> readings = generateWaterReadings(reportDateRange, coldWaterSectionInputs.readings());

		List<WaterFee> priceList = generatePriceList(reportDateRange, coldWaterSectionInputs.priceList(), readings);

		BigDecimal totalAmount = calculateAmount(priceList, WaterFee::periodAmount);

		return new ColdWaterSection(coldWaterSectionInputs.name(), totalAmount, readings, priceList);
	}
}
