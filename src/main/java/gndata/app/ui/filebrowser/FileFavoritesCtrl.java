package gndata.app.ui.filebrowser;

import com.google.inject.Inject;
import gndata.app.state.FileNavigationState;
import gndata.app.state.ProjectState;
import gndata.lib.srv.FileAdapter;
import gndata.lib.srv.LocalFile;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 * Created by msonntag on 02.12.14.
 */
public class FileFavoritesCtrl implements Initializable {

    @FXML
    private ListView<FileAdapter> fileFavorites;

    @FXML
    private Button openFileFavoriteHandling;

    private final FileNavigationState navState;

    private final ProjectState projectState;

    @Inject
    public FileFavoritesCtrl(ProjectState projectState, FileNavigationState navState){



        this.projectState = projectState;
        this.navState = navState;

        projectState.configProperty().addListener((p, o, n) -> {
            if (n == null)
                return;

            Path path = Paths.get(n.getProjectPath());
            this.navState.getFavoriteFolders().add(new LocalFile(path));
            this.navState.getFavoriteFolders().add(new LocalFile(path.resolve(Paths.get("schemas"))));
        });


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        fileFavorites.setItems(navState.getFavoriteFolders());
        navState.selectedFileProperty().bind(fileFavorites.getSelectionModel().selectedItemProperty());
        fileFavorites.addEventHandler(MouseEvent.MOUSE_CLICKED, new SelectFileFavoriteHandler());
    }

    private class SelectFileFavoriteHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            FileAdapter fa = fileFavorites.getSelectionModel().getSelectedItem();
            // TODO this check should not be required, only directories should be eligible as favorite items
            if (! fa.isDirectory()) {
                return;
            }
            navState.setSelectedParent(fa);
        }
    }

}
