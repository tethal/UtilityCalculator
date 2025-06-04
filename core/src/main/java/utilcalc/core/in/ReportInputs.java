package utilcalc.core.in;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("startDate and endDate must be non-null");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate must not be before startDate");
        }
        if (tenant == null || tenant.isEmpty()) {
            throw new IllegalArgumentException("tenant must be non-empty");
        }
        if (owner == null || owner.isEmpty()) {
            throw new IllegalArgumentException("owner must be non-empty");
        }
        if (reportPlace == null || reportPlace.isBlank()) {
            throw new IllegalArgumentException("reportPlace must be non-empty");
        }
        if (reportDate == null) {
            throw new IllegalArgumentException("reportDate must be non-null");
        }
        if (sections.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("sections must not contain null elements");
        }

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
