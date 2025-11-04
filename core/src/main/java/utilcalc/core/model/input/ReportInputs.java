package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.*;

import java.time.LocalDate;
import java.util.List;
import utilcalc.core.model.DateRange;

public record ReportInputs(DateRange dateRange, List<String> tenant, List<String> owner, String reportPlace,
		LocalDate reportDate, List<String> sources, List<SectionInputs> sections) {
	public ReportInputs {
		ensureNonNull(dateRange, "dateRange");
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
