package gndata.app.ui.tree;

import gndata.app.state.ProjectState;
import gndata.lib.config.ProjectConfig;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.inject.Inject;


/**
 * Controller for the metadata tree view.
 */
public class MetadataTreeCtrl {

    @FXML private TreeView<String> metadataTreeView;

    @Inject
    public MetadataTreeCtrl(ProjectState projectState) {
        projectState.addListener((observable, oldVal, newVal) -> loadTree(newVal));
    }

    public void initialize() {
        loadTree("initial 1", "initial 2", "initial 3");
    }

    public void loadTree(ProjectConfig config) {
        // TODO load tree from metadata files

        System.out.println("refreshTree");
    }

    public void loadTree(String... rootItems) {
        TreeItem<String> root = new TreeItem<String>("Root Node");
        root.setExpanded(true);
        for (String itemString: rootItems) {
            root.getChildren().add(new TreeItem<String>(itemString));
        }

        metadataTreeView.setRoot(root);
    }
}
