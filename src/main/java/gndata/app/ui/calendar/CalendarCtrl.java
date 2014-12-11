package gndata.app.ui.calendar;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javafx.fxml.*;
import javafx.scene.layout.BorderPane;

import gndata.app.ui.calendar.favorites.CalendarFavoritesView;


public class CalendarCtrl implements Initializable {

    @FXML
    private BorderPane view;

    private final TimelineView timeline;
    private final CalendarFavoritesView favview;

    @Inject
    public CalendarCtrl(TimelineView timeline, CalendarFavoritesView favview) {
        this.timeline = timeline;
        this.favview = favview;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            view.setTop(timeline.getScene());
            view.setLeft(favview.getScene());

            /* TODO

            splitPane.getItems().add(0, listView.getScene());
            splitPane.getItems().add(1, detailsView.getScene());
            */
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
