package gndata.app.ui.calendar;

import gndata.app.ui.filebrowser.FileDetailsView;
import gndata.app.ui.filebrowser.FileFavoritesView;
import gndata.app.ui.filebrowser.FileListView;
import gndata.app.ui.filebrowser.FileNavigationView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class CalendarCtrl implements Initializable{

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
