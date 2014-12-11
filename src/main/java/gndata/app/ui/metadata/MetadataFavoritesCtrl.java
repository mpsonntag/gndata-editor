package gndata.app.ui.metadata;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javafx.fxml.*;
import javafx.scene.control.ListView;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.vocabulary.*;
import gndata.app.state.*;
import gndata.lib.srv.ResourceAdapter;

/**
 * Controller for the metadata favorites list.
 */
public class MetadataFavoritesCtrl implements Initializable {
    @FXML
    private ListView<ResourceAdapter> favoritesList;

    private final ProjectState projectState;
    private final MetadataNavState navState;

    @Inject
    public MetadataFavoritesCtrl(ProjectState projectState, MetadataNavState navState) {
        this.projectState = projectState;
        this.navState = navState;

        this.projectState.configProperty().addListener((p, o, n) -> {
            navState.getFavoriteFolders().setAll(
                    projectState.getMetadata().getAvailableTypes().stream()
                            .map(r -> new ResourceAdapter(r, null))
                            .collect(Collectors.toList())
            );
        });
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        favoritesList.setItems(navState.getFavoriteFolders());
        favoritesList.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> {
            if (n == null)
                return;

            navState.setSelectedParent(n);
        });
    }
}
