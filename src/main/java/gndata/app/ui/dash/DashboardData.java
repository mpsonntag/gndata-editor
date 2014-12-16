package gndata.app.ui.dash;

/**
 * Created by stoewer on 16.12.14.
 */
public class DashboardData {

    String name;
    String description;

    public DashboardData(String name, String dscription) {
        this.name = name;
        this.description = dscription;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
