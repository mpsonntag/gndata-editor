package gndata.app.ui.metadata;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.TextField;

import com.google.inject.Inject;
import gndata.app.state.*;
import gndata.app.ui.util.*;
import gndata.lib.srv.ResourceAdapter;

/**
 * Controller for the metadata navigation bar.
 */
public class MetadataNavCtrl implements Initializable {
    @FXML
    private TogglePane togglePane;
    @FXML
    private TextField searchField;
    @FXML
    private BreadCrumbNav<ResourceAdapter> navBar;

    private final ProjectState projectState;
    private final MetadataNavState navState;

    private final BooleanProperty showBreadCrumbs;

    @Inject
    public MetadataNavCtrl(ProjectState projectState, MetadataNavState navState) {
        this.projectState = projectState;
        this.navState = navState;

        showBreadCrumbs = new SimpleBooleanProperty(true);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        navBar.setItems(navState.getNavigationPath());

        navBar.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> navState.setSelectedParent(n));
        navState.selectedParentProperty().addListener((p, o, n) -> navBar.getSelectionModel().select(n));

        projectState.configProperty().addListener((p, o, n) -> {
            if (navState.getFavoriteFolders().size() > 0) {
                navState.setSelectedParent(navState.getFavoriteFolders().get(0));
            }
        });

        togglePane.showFirstProperty().bindBidirectional(showBreadCrumbs);
    }

    public void toggleNavBar() {
        showBreadCrumbs.set(! showBreadCrumbs.get());
    }
}
