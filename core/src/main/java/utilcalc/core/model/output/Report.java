package utilcalc.core.model.output;

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
        tenant = List.copyOf(tenant);
        owner = List.copyOf(owner);
        sources = List.copyOf(sources);
        sections = List.copyOf(sections);
    }
}
