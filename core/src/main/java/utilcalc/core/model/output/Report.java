package utilcalc.core.model.output;

import static utilcalc.core.utils.Util.*;

import java.time.LocalDate;
import java.util.List;

public record Report(
        LocalDate startDate,
        LocalDate endDate,
        List<String> tenant,
        List<String> owner,
        String reportPlace,
        LocalDate reportDate,
        List<String> sources,
        List<ReportSection> sections) {

    public Report {
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
