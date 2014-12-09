package gndata.app.ui.metadata;

import javax.inject.Inject;

import com.google.inject.Injector;
import gndata.app.ui.util.InjectorView;

/**
 * List view of the metadata browser.
 */
public class MetadataListView extends InjectorView {

    @Inject
    public MetadataListView(Injector injector) {
        super(injector);
    }
}
