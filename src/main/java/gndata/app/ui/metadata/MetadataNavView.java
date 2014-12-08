package gndata.app.ui.metadata;

import javax.inject.Inject;

import com.google.inject.Injector;
import gndata.app.ui.util.InjectorView;

/**
 * View for the navigation bar of the metadata browser.
 */
public class MetadataNavView extends InjectorView {

    @Inject
    public MetadataNavView(Injector injector) {
        super(injector);
    }
}
