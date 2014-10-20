package gndata.app.ui.tree;

import com.google.inject.*;
import gndata.app.ui.util.DIView;

/**
 * Tree view for the project metadata.
 */
public class MetadataTreeView extends DIView {

    @Inject
    public MetadataTreeView(Injector injector) {
        super(injector);
    }

}