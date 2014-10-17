package gndata.app.ui.tree.metadata;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import gndata.app.state.ProjectState;
import gndata.app.ui.util.RDFTreeItem;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;


/**
 * Controller for the metadata tree view.
 */
public class MetadataTreeCtrl {

    private final ProjectState projectState;

    @FXML
    private TreeView<RDFNode> metadataTreeView;

    @FXML
    private TextField searchInput;

    @FXML
    private BorderPane metadataPane;

    @Inject
    public MetadataTreeCtrl(ProjectState projectState) {
        this.projectState = projectState;
    }

    public void initialize() {
        // listener to reload the tree if project changes
        projectState.addListener((observable, oldVal, newVal) -> loadTree(""));

        // listener to reload tree if search text is entered
        searchInput.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                loadTree(searchInput.getText());
            }
        });
        searchInput.setText("type search text here and press Enter");

        // facade for the metadata tree items
        metadataTreeView.setCellFactory((p) -> new MetadataTreeCell());

        /*
        // TODO listener for tree item selection
        this.metadataTreeView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldVal, newVal) ->
                        System.out.println("Selected"));
        */

        loadTree("");
    }

    public TreeView<RDFNode> getTree() {
        return metadataTreeView;
    }

    private void loadTree(String filter) {
        if (projectState.getMetadata() != null) {
            Model annotations = projectState.getMetadata().getAnnotations(filter);

            TreeItem<RDFNode> fakeRoot = new TreeItem<>(annotations.getResource("Metadata"));
            fakeRoot.getChildren().addAll(RDFTreeItem.getRootClasses(annotations));

            metadataTreeView.setRoot(fakeRoot);
            metadataTreeView.setShowRoot(false);
        } else {
            metadataTreeView.setRoot(null);
        }
    }
}
