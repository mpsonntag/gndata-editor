package gndata.app.ui.filebrowser;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javafx.fxml.*;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

/**
 * Created by msonntag on 02.12.14.
 */
public class FileBrowserCtrl implements Initializable{

    @FXML
    private BorderPane view;
    @FXML
    private SplitPane splitPane;

    private FileDetailsView detailsView;
    private FileListView listView;
    private FileNavigationView navigationView;
    private FileFavoritesView favoritesView;

    @Inject
    public FileBrowserCtrl(FileDetailsView detailsView, FileListView listView, FileNavigationView navigationView,
                           FileFavoritesView favoritesView) {
        this.detailsView = detailsView;
        this.listView = listView;
        this.navigationView = navigationView;
        this.favoritesView = favoritesView;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            view.setTop(navigationView.getScene());
            view.setLeft(favoritesView.getScene());
            splitPane.getItems().add(0, listView.getScene());
            splitPane.getItems().add(1, detailsView.getScene());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
