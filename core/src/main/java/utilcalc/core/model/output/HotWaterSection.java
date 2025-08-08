package utilcalc.core.model.output;

import static utilcalc.core.utils.Util.*;

import java.math.BigDecimal;
import java.util.List;

public record HotWaterSection(
        String name,
        BigDecimal totalAmount,
        List<WaterReading> readings,
        List<WaterFee> priceList,
        List<WaterHeatingBasicPart> heatingBasicParts,
        List<WaterHeatingConsumablePart> heatingConsumableParts)
        implements ReportSection {

    public HotWaterSection {
        ensureNonBlank(name, "name");
        ensureNonNull(totalAmount, "totalAmount");
        ensureNonEmpty(readings, "readings");
        ensureNonEmpty(priceList, "priceList");
        ensureNonEmpty(heatingBasicParts, "heatingBasicParts");
        ensureNonEmpty(heatingConsumableParts, "heatingConsumableParts");

        readings = List.copyOf(readings);
        priceList = List.copyOf(priceList);
        heatingBasicParts = List.copyOf(heatingBasicParts);
        heatingConsumableParts = List.copyOf(heatingConsumableParts);
    }
}
