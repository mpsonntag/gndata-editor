package gndata.app.ui.tree;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.OWL;
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

        // listener to reload the tree if project changes
        this.projectState.addListener((observable, oldVal, newVal) -> loadTree());
    }

    public void initialize() {
        loadTree();
    }

    public void loadTree() {
        if (projectState.getMetadata() != null) {
            InfModel model = projectState.getMetadata().getModel();

            TreeItem<Resource> fakeRoot = new TreeItem<>(model.getResource("FakeRoot"));
            //TreeItem<Resource> fakeRoot = new TreeItem<>(OWL.Thing);
            fakeRoot.getChildren().addAll(RDFTreeItem.getRootItems(model));

            //TreeItem<Resource> root = new RDFTreeItem(model, OWL.Thing);

            metadataTreeView.setRoot(fakeRoot);
            //metadataTreeView.setShowRoot(false);
        } else {
            metadataTreeView.setRoot(null);
        }
    }
}
