package utilcalc.core.model.output;

import static utilcalc.core.utils.Util.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import utilcalc.core.model.DateRange;

public record Report(
        DateRange dateRange,
        List<String> tenant,
        List<String> owner,
        String reportPlace,
        LocalDate reportDate,
        List<String> sources,
        List<ReportSection> sections,
        BigDecimal total) {

    public Report {
        ensureNonNull(dateRange, "dateRange");
        ensureNonEmpty(tenant, "tenant");
        ensureNonEmpty(owner, "owner");
        ensureNonBlank(reportPlace, "reportPlace");
        ensureNonNull(reportDate, "reportDate");
        ensureNoNullElements(sections, "sections");
        ensureNonNull(total, "total");

        tenant = List.copyOf(tenant);
        owner = List.copyOf(owner);
        sources = sources != null ? List.copyOf(sources) : List.of();
        sections = List.copyOf(sections);
    }
}
