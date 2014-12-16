package gndata.app.ui.dash;

import java.util.*;
import static java.util.Collections.*;

/**
 * Dummy implementation for dashboard data.
 */
public class DashboardData {

    private String name;
    private String description;

    private List<String> scientists;
    private List<String> experiments;
    private List<String> recentSessions;

    private Integer fileCount;
    private Integer metadataCount;

    public DashboardData() {
        name = "Fish Project One";

        description =
                "Lorem ipsum dolor sit amet, consectetur adipisici elit, sed " +
                "eiusmod tempor incidunt ut labore et dolore magna aliqua. ";
        description += description += description;

        scientists = new LinkedList<>();
        addAll(scientists, "Jan Grewe", "Jan Bender", "Thomas Wachtler");

        experiments = new LinkedList<>();
        addAll(experiments, "Fish tracker", "Fried fish tracker");

        recentSessions = new LinkedList<>();
        addAll(recentSessions, "Session 2014-10-20", "Session 2014-09-06");

        fileCount = 123;
        metadataCount = 309234;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getScientists() {
        return scientists;
    }

    public List<String> getExperiments() {
        return experiments;
    }

    public List<String> getRecentSessions() {
        return recentSessions;
    }

    public Integer getFileCount() {
        return fileCount;
    }

    public Integer getMetadataCount() {
        return metadataCount;
    }
}
