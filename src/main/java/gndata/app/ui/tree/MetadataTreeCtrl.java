package gndata.app.ui.tree;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import gndata.app.state.ProjectState;
import gndata.app.ui.util.RDFTreeItem;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javax.inject.Inject;


/**
 * Controller for the metadata tree view.
 */
public class MetadataTreeCtrl {

    @FXML private TreeView<Resource> metadataTreeView;

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

    public TreeView<Resource> getTree() {
        return metadataTreeView;
    }

    private void loadTree() {
        if (projectState.getMetadata() != null) {
            OntModel model = projectState.getMetadata().getSchema();
            Model annotations = projectState.getMetadata().getAnnotations();

            TreeItem<Resource> fakeRoot = new TreeItem<>(model.getResource("Metadata"));
            fakeRoot.getChildren().addAll(RDFTreeItem.getRootClasses(model, annotations));

            metadataTreeView.setRoot(fakeRoot);
        } else {
            metadataTreeView.setRoot(null);
        }
    }
}
