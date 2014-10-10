package gndata.app.ui.tree;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import gndata.app.state.ProjectState;
import gndata.app.ui.util.RDFTreeItem;
import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

import javax.inject.Inject;


/**
 * Controller for the metadata tree view.
 */
public class MetadataTreeCtrl {

    @FXML private TreeView<RDFNode> metadataTreeView;

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
        metadataTreeView.setCellFactory((p) -> new MetadataTreeCell());

        loadTree();
    }

    public TreeView<RDFNode> getTree() {
        return metadataTreeView;
    }

    private void loadTree() {
        if (projectState.getMetadata() != null) {
            Model annotations = projectState.getMetadata().getAnnotations();

            TreeItem<RDFNode> fakeRoot = new TreeItem<>(annotations.getResource("Metadata"));
            fakeRoot.getChildren().addAll(RDFTreeItem.getRootClasses(annotations));

            metadataTreeView.setRoot(fakeRoot);
            metadataTreeView.setShowRoot(false);
        } else {
            metadataTreeView.setRoot(null);
        }
    }
}
