package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.*;

import java.time.LocalDate;
import java.util.List;

public record ReportInputs(
        LocalDate startDate,
        LocalDate endDate,
        List<String> tenant,
        List<String> owner,
        String reportPlace,
        LocalDate reportDate,
        List<String> sources,
        List<SectionInputs> sections) {
    public ReportInputs {
        ensureNonNull(startDate, "startDate");
        ensureNonNull(endDate, "endDate");
        ensureValidDateRange(startDate, endDate);
        ensureNonEmpty(tenant, "tenant");
        ensureNonEmpty(owner, "owner");
        ensureNonBlank(reportPlace, "reportPlace");
        ensureNonNull(reportDate, "reportDate");
        ensureNoNullElements(sections, "sections");

        tenant = List.copyOf(tenant);
        owner = List.copyOf(owner);
        sources = sources != null ? List.copyOf(sources) : List.of();
        sections = List.copyOf(sections);
    }
}
