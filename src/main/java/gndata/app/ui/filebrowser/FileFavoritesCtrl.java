package gndata.app.ui.filebrowser;

import com.google.inject.Inject;
import gndata.app.state.FileNavigationState;
import gndata.app.state.ProjectState;
import gndata.lib.srv.FileAdapter;
import gndata.lib.srv.LocalFile;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

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

    @Inject
    public FileFavoritesCtrl(ProjectState projectState, FileNavigationState navState){

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

        fileFavorites.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> {
            if (n == null)
                return;
            navState.setSelectedParent(n);
        });
    }
}
