package gndata.app.ui.dash;

import javax.inject.Inject;

import com.google.inject.Injector;
import gndata.app.ui.util.InjectorView;

/**
 * View for the dashboard
 */
public class DashboardView extends InjectorView {

    @Inject
    public DashboardView(Injector injector) {
        super(injector);
    }
}
