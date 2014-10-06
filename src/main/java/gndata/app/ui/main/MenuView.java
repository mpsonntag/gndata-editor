package gndata.app.ui.main;

import com.google.inject.Injector;
import gndata.app.ui.util.DIView;

import javax.inject.Inject;

/**
 * The view for the main menu bar.
 */
public class MenuView  extends DIView {

    @Inject
    public MenuView(Injector injector) {
        super(injector);
    }

}
