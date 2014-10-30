// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.metadata.tree;

import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;
import gndata.app.state.*;

/**
 * Controller for the metadata tree view.
 */
public class RDFTreeCtrl implements Initializable {

    private final ProjectState projectState;
    private final MetadataState metadataState;

    @FXML
    private TreeView<RDFNode> metadataTreeView;
    @FXML
    private TextField searchInput;

    @Inject
    public RDFTreeCtrl(ProjectState projectState, MetadataState metadataState) {
        this.projectState = projectState;
        this.metadataState = metadataState;
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // listener to reload the tree if project changes
        projectState.addListener((observable, oldVal, newVal) -> loadTree(null));

        // bind selection to metadata state
        metadataTreeView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            metadataState.setSelectedNode(newVal != null ? newVal.getValue() : null);
        });

        // listener to reload tree if search text is entered
        searchInput.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                String input = searchInput.getText();
                loadTree(input.equals("") ? null : input);
            }
        });
        searchInput.setText("type search text here and press Enter");

        // facade for the metadata tree items
        metadataTreeView.setCellFactory((p) -> new RDFTreeCell());

        loadTree(null);
    }

    public TreeView<RDFNode> getTree() {
        return metadataTreeView;
    }

    private void loadTree(String filter) {
        if (projectState.getMetadata() != null) {
            Model annotations = projectState.getMetadata().getAnnotations();
            TreeItem<RDFNode> fakeRoot = new TreeItem<>(annotations.getResource("Metadata"));

            ObservableList<RDFTreeItem> items = filter == null ? getRootClasses() : getRootClasses(filter);

            // sorting in alphabetical order
            items.sort((a, b) -> a.toString().compareTo(b.toString()));

            fakeRoot.getChildren().addAll(items);

            metadataTreeView.setRoot(fakeRoot);
            metadataTreeView.setShowRoot(false);
        } else {
            metadataTreeView.setRoot(null);
        }
    }

    /**
     * Returns a list of available types (triple used with RDF.type) from
     * the metadata of the current project.
     *
     * @return list of TreeItem(s) representing top classes
     */
    public ObservableList<RDFTreeItem> getRootClasses() {
        Model annotations = projectState.getMetadata().getAnnotations();
        ObservableList<RDFTreeItem> items = FXCollections.observableArrayList();

        NodeIterator iter = annotations.listObjectsOfProperty(RDF.type);
        while (iter.hasNext()) {
            RDFNode st = iter.next();
            if (st.isResource()) {
                Resource r = st.asResource();

                // exclude OWL definitions from the root items
                if (r.getNameSpace() != null && !r.getNameSpace().equals(OWL.getURI())) {
                    items.add(new RDFTreeItem(r));
                }
            }
        }

        return items;
    }

    /**
     * Returns a list of RDF Tree items filtered by literal in the
     * the metadata of the current project.
     *
     * @return list of TreeItem(s) representing top classes
     */
    public ObservableList<RDFTreeItem> getRootClasses(String filter) {
        Model annotations = projectState.getMetadata().getAnnotations();
        Model selection = projectState.getMetadata().getAnnotations(filter);

        ObservableList<RDFTreeItem> items = FXCollections.observableArrayList();
        selection.listSubjects()
                .toList()
                .forEach(a -> items.add(new RDFTreeItem(annotations.getResource(a.getURI()))));

        return items;
    }
}
