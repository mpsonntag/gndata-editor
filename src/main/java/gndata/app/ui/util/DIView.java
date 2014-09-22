package gndata.app.ui.util;

import com.google.inject.Injector;

import javax.inject.Inject;

/**
 * Base class for views that use dependency injection (Guice) for controller instantiation.
 */
public class DIView extends AbstractView {

    @Inject
    private Injector injector;

    public DIView() {
        super();

        getLoader().setControllerFactory(cls -> injector.getInstance(cls));
    }

}
