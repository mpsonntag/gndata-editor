package gndata.app.ui.tree.files;

import gndata.app.state.ProjectState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.io.File;


/**
 * Controller for the file tree view.
 */
public class FileTreeCtrl {

    private final ProjectState projectState;

    @FXML
    private TreeView<File> fileTreeView;

    @FXML
    private BorderPane fileTreePane;

    @Inject
    public FileTreeCtrl(ProjectState projectState) {
        this.projectState = projectState;
    }

    public void initialize() {
        // listener to reload the tree if project changes
        projectState.addListener((observable, oldVal, newVal) -> loadTree());

        // facade for the metadata tree items
        fileTreeView.setCellFactory((p) -> new FileTreeCell());

        loadTree();
    }

    private void loadTree() {
        if (projectState.getConfig() != null) {
            String path = projectState.getConfig().getProjectPath();
            TreeItem<File> root = createNode(new File(path));

            fileTreeView.setRoot(root);
            fileTreeView.setShowRoot(false);
        } else {
            fileTreeView.setRoot(null);
        }
    }

    // This method creates a TreeItem to represent the given File. It does this
    // by overriding the TreeItem.getChildren() and TreeItem.isLeaf() methods
    // anonymously, but this could be better abstracted by creating a
    // 'FileTreeItem' subclass of TreeItem. However, this is left as an exercise
    // for the reader.
    private TreeItem<File> createNode(final File f) {
        return new TreeItem<File>(f) {
            private boolean isLeaf;
            private boolean isFirstTimeChildren = true;
            private boolean isFirstTimeLeaf = true;

            @Override public ObservableList<TreeItem<File>> getChildren() {
                if (isFirstTimeChildren) {
                    isFirstTimeChildren = false;

                    // First getChildren() call, so we actually go off and
                    // determine the children of the File contained in this TreeItem.
                    super.getChildren().setAll(buildChildren(this));
                }
                return super.getChildren();
            }

            @Override public boolean isLeaf() {
                if (isFirstTimeLeaf) {
                    isFirstTimeLeaf = false;
                    File f = (File) getValue();
                    isLeaf = f.isFile();
                }

                return isLeaf;
            }

            private ObservableList<TreeItem<File>> buildChildren(TreeItem<File> TreeItem) {
                File f = TreeItem.getValue();
                if (f != null && f.isDirectory()) {
                    File[] files = f.listFiles();
                    if (files != null) {
                        ObservableList<TreeItem<File>> children = FXCollections.observableArrayList();

                        for (File childFile : files) {
                            children.add(createNode(childFile));
                        }

                        return children;
                    }
                }

                return FXCollections.emptyObservableList();
            }
        };
    }
}
