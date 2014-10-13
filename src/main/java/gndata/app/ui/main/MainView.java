package gndata.app.ui.main;

import com.google.inject.Injector;
import gndata.app.ui.util.DIView;

import javax.inject.Inject;

/**
 * The main view of the application.
 */
public class MainView extends DIView {

    @Inject
    public MainView(Injector injector) {
        super(injector);
    }

}
