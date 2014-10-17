package gndata.app.ui.tree.files;

import com.google.inject.Inject;
import com.google.inject.Injector;
import gndata.app.ui.util.DIView;

/**
 * Tree view for the project metadata.
 */
public class FileTreeView extends DIView {

    @Inject
    public FileTreeView(Injector injector) {
        super(injector);
    }

}