package gndata.app.ui.calendar.favorites;

import javax.inject.Inject;

import com.google.inject.Injector;
import gndata.app.ui.util.InjectorView;

/**
 * The favorites panel of the calendar view.
 */
public class CalendarFavoritesView extends InjectorView {

    @Inject
    public CalendarFavoritesView(Injector injector) {
        super(injector);
    }
}
