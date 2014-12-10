package gndata.app.ui.metadata;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;

import com.google.inject.Inject;
import gndata.app.state.*;
import gndata.app.ui.util.BreadCrumbNav;
import gndata.lib.srv.ResourceAdapter;

/**
 * Controller for the metadata navigation bar.
 */
public class MetadataNavCtrl implements Initializable {

    @FXML
    private BreadCrumbNav<ResourceAdapter> navBar;

    private final ProjectState projectState;
    private final MetadataNavState navState;

    @Inject
    public MetadataNavCtrl(ProjectState projectState, MetadataNavState navState) {
        this.projectState = projectState;
        this.navState = navState;
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
    }
}
