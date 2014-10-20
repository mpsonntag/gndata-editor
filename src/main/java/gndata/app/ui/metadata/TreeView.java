package gndata.app.ui.metadata;

import com.google.inject.*;
import gndata.app.ui.util.DIView;

/**
 * Tree view for the project metadata.
 */
public class TreeView extends DIView {

    @Inject
    public TreeView(Injector injector) {
        super(injector);
    }

}