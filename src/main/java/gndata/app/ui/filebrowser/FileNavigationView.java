package gndata.app.ui.filebrowser;

import javax.inject.Inject;

import com.google.inject.Injector;
import gndata.app.ui.util.InjectorView;

/**
 * Created by msonntag on 02.12.14.
 */
public class FileNavigationView extends InjectorView {

    @Inject
    public FileNavigationView(Injector injector) {
        super(injector);
    }
}
