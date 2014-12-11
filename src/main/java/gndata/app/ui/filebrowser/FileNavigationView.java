package gndata.app.ui.filebrowser;

import javax.inject.Inject;

import com.google.inject.Injector;
import gndata.app.ui.util.InjectorView;

/**
 * View for the navigation bar of the file browser
 */
public class FileNavigationView extends InjectorView {

    @Inject
    public FileNavigationView(Injector injector) {
        super(injector);
    }
}
