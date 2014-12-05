package gndata.app.ui.filebrowser;

import javax.inject.Inject;

import com.google.inject.Injector;
import gndata.app.ui.util.InjectorView;

/**
 * Created by msonntag on 02.12.14.
 */
public class FileListView extends InjectorView {

    @Inject
    public FileListView(Injector injector) {
        super(injector);
    }
}
