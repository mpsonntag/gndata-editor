package gndata.app.ui.tree;

import com.hp.hpl.jena.ontology.OntModel;
import gndata.app.state.ProjectState;
import gndata.app.ui.util.RDFTreeItem;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javax.inject.Inject;


/**
 * Controller for the metadata tree view.
 */
public class MetadataTreeCtrl {

    @FXML private TreeView<String> metadataTreeView;

    private final ProjectState projectState;

    @Inject
    public MetadataTreeCtrl(ProjectState projectState) {
        this.projectState = projectState;

        /*
        // TODO listener for tree item selection
        this.metadataTreeView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldVal, newVal) ->
                        System.out.println("Selected"));
        */

        // listener to reload the tree if project changes
        this.projectState.addListener((observable, oldVal, newVal) -> loadTree());
    }

    public void initialize() {
        loadTree();
    }

    public void loadTree() {
        if (projectState.getMetadata() != null) {
            OntModel model = projectState.getMetadata().getSchema();

            TreeItem<String> schemaRoot = new TreeItem<>("Ontology");
            schemaRoot.getChildren().addAll(RDFTreeItem.getRootClasses(model));

            TreeItem<String> annotationsRoot = new TreeItem<>("Annotations");
            // TODO add annotations

            TreeItem<String> fakeRoot = new TreeItem<>("Fake Root");

            fakeRoot.getChildren().addAll(schemaRoot, annotationsRoot);
            metadataTreeView.setRoot(fakeRoot);
            metadataTreeView.setShowRoot(false);
        } else {
            metadataTreeView.setRoot(null);
        }
    }
}
