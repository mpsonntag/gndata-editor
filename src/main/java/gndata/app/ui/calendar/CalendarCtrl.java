package gndata.app.ui.calendar;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javafx.fxml.*;
import javafx.scene.layout.BorderPane;


public class CalendarCtrl implements Initializable {

    @FXML
    private BorderPane view;

    private final TimelineView timeline;

    @Inject
    public CalendarCtrl(TimelineView timeline) {
        this.timeline = timeline;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            view.setTop(timeline.getScene());

            /* TODO
            view.setLeft(favoritesView.getScene());

            splitPane.getItems().add(0, listView.getScene());
            splitPane.getItems().add(1, detailsView.getScene());
            */
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
