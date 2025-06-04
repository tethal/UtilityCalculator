package utilcalc.core.model.output;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a complete report containing metadata and a collection of sections.
 *
 * <p>This class is immutable, all fields are final and set via constructor.
 */
public final class Report {

    /** The start date of the billing period */
    private final LocalDate startDate;

    /** The end date of the billing period */
    private final LocalDate endDate;

    /**
     * List of String data about tenant
     *
     * <p>Include: name, address
     */
    private final List<String> tenant;

    /**
     * List of String data about owner
     *
     * <p>Include: name, contact
     */
    private final List<String> owner;

    private final String reportPlace;

    private final LocalDate reportDate;

    /** List of sources for payments and amount data */
    private final List<String> sources;

    /** List of report sections detailing individual services. */
    private final List<ReportSection> sections;

    public Report(
            LocalDate startDate,
            LocalDate endDate,
            List<String> tenant,
            List<String> owner,
            String reportPlace,
            LocalDate reportDate,
            List<String> sources,
            List<ReportSection> sections) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.tenant = List.copyOf(tenant);
        this.owner = List.copyOf(owner);
        this.reportPlace = reportPlace;
        this.reportDate = reportDate;
        this.sources = List.copyOf(sources);
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

    public List<ReportSection> getSections() {
        return sections;
    }

}
