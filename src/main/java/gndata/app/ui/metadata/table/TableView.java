package gndata.app.ui.metadata.table;

import com.google.inject.Inject;
import com.google.inject.Injector;
import gndata.app.ui.util.DIView;

/**
 * Table view for the selected metadata item.
 */
public class TableView extends DIView {

    @Inject
    public TableView(Injector injector) {
        super(injector);
    }

}