package gndata.app.ui.filebrowser;

import java.net.URL;
import java.util.*;
import javax.inject.Inject;
import javafx.fxml.*;

import gndata.app.state.*;
import gndata.app.ui.util.BreadCrumbNav;
import gndata.lib.srv.*;

/**
 * Created by msonntag on 02.12.14.
 */
public class FileNavigationCtrl implements Initializable {

    @FXML
    private BreadCrumbNav<FileAdapter> navBar;

    private final ProjectState projectState;
    private final FileNavigationState navState;

    @Inject
    public FileNavigationCtrl (FileNavigationState navState, ProjectState projectState) {

        this.projectState = projectState;
        this.navState = navState;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        navBar.setItems(navState.getNavigationPath());

        // ensure bidirectional changes in navigation bar and navigation state
        navBar.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> navState.setSelectedParent(n));
        navState.selectedParentProperty().addListener((p, o, n) -> navBar.getSelectionModel().select(n));

        projectState.configProperty().addListener(
                (b, o, n) -> navState.getNavigationPath().add(navState.getFavoriteFolders().get(0))
        );
    }

}