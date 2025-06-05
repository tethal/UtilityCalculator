package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.*;

import java.time.LocalDate;
import java.util.List;

public final class ReportInputs {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<String> tenant;
    private final List<String> owner;
    private final String reportPlace;
    private final LocalDate reportDate;
    private final List<String> sources;
    private final List<SectionInputs> sections;

    public ReportInputs(
            LocalDate startDate,
            LocalDate endDate,
            List<String> tenant,
            List<String> owner,
            String reportPlace,
            LocalDate reportDate,
            List<String> sources,
            List<SectionInputs> sections) {

        ensureNonNull(startDate, "startDate");
        ensureNonNull(endDate, "endDate");
        ensureValidDateRange(startDate, endDate);
        ensureNonEmpty(tenant, "tenant");
        ensureNonEmpty(owner, "owner");
        ensureNonBlank(reportPlace, "reportPlace");
        ensureNonNull(reportDate, "reportDate");
        ensureNoNullElements(sections, "sections");

        this.startDate = startDate;
        this.endDate = endDate;
        this.tenant = List.copyOf(tenant);
        this.owner = List.copyOf(owner);
        this.reportPlace = reportPlace;
        this.reportDate = reportDate;
        this.sources = sources != null ? List.copyOf(sources) : List.of();
        this.sections = List.copyOf(sections);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public List<String> getTenant() {
        return tenant;
    }

    public List<String> getOwner() {
        return owner;
    }

    public String getReportPlace() {
        return reportPlace;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public List<String> getSources() {
        return sources;
    }

    public List<SectionInputs> getSections() {
        return sections;
    }

    @Override
    public String toString() {
        return "ReportInputs ["
                + "startDate="
                + startDate
                + ", endDate="
                + endDate
                + ", tenant="
                + tenant
                + ", owner="
                + owner
                + ", reportPlace='"
                + reportPlace
                + '\''
                + ", reportDate="
                + reportDate
                + ", sources="
                + sources
                + ", sections="
                + sections
                + ']';
    }
}
