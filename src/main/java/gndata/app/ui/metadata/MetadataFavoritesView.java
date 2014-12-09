package gndata.app.ui.metadata;

import javax.inject.Inject;

import com.google.inject.Injector;
import gndata.app.ui.util.InjectorView;

/**
 * The favorites panel of the metadata browser.
 */
public class MetadataFavoritesView extends InjectorView {

    @Inject
    public MetadataFavoritesView(Injector injector) {
        super(injector);
    }
}
