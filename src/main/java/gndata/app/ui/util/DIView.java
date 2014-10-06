package gndata.app.ui.util;

import com.google.inject.Injector;

import javax.inject.Inject;

/**
 * Base class for views that use dependency injection (Guice) for controller instantiation.
 */
public class DIView extends AbstractView {

    private final Injector injector;

    @Inject
    public DIView(Injector injector) {
        super();
        this.injector = injector;
        getLoader().setControllerFactory(cls -> this.injector.getInstance(cls));
    }

}
