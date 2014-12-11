package gndata.app.ui.filebrowser;

import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.scene.control.TextField;

import gndata.app.state.*;
import gndata.app.ui.util.*;
import gndata.lib.srv.FileAdapter;

/**
 * Controller for {@link FileNavigationView}
 */
public class FileNavigationCtrl implements Initializable {
    @FXML
    private TogglePane togglePane;
    @FXML
    private TextField searchFiled;
    @FXML
    private BreadCrumbNav<FileAdapter> navBar;

    private final ProjectState projectState;
    private final FileNavigationState navState;
    private final BooleanProperty showBreadCrumbs;

    @Inject
    public FileNavigationCtrl (FileNavigationState navState, ProjectState projectState) {

        this.projectState = projectState;
        this.navState = navState;
        showBreadCrumbs = new SimpleBooleanProperty(true);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        navBar.setItems(navState.getNavigationPath());

        // ensure bidirectional changes in navigation bar and navigation state
        navBar.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> navState.setSelectedParent(n));
        navState.selectedParentProperty().addListener((p, o, n) -> navBar.getSelectionModel().select(n));

        projectState.configProperty().addListener((b, o, n) -> {
            if (n == null || navState.getFavoriteFolders().size() < 1)
                return;

            navState.getNavigationPath().add(navState.getFavoriteFolders().get(0));
        });

        togglePane.showFirstProperty().bindBidirectional(showBreadCrumbs);

    }

    public void toggleNavBar() {
        showBreadCrumbs.set(! showBreadCrumbs.get());
    }

    public void goBack() {
        // TODO implement
        System.out.println("FileNavCtrl: goBack");
    }

    public void goForward() {
        // TODO implement
        System.out.println("FileNavCtrl: goForward");
    }
}
