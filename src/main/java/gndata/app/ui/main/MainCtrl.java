// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javafx.fxml.*;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

import gndata.app.ui.metadata.table.*;
import gndata.app.ui.metadata.tree.*;

/**
 * Controller for the main application window.
 */
public class MainCtrl implements Initializable {

    @FXML
    public BorderPane view;
    @FXML
    private SplitPane splitPane;
    @Inject
    private MenuView menuView;

    @Inject
    private RDFTreeView metadataView;
    @Inject
    private RDFTableView RDFTableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // top Menu bar
            view.setTop(menuView.getScene());

            // split pane with metadata tree
            splitPane.getItems().add(metadataView.getScene());
            splitPane.getItems().add(RDFTableView.getScene());

            RDFTreeCtrl treeCtrl = metadataView.getLoader().getController();
            RDFTableCtrl tableCtrl = RDFTableView.getLoader().getController();

            // listener to update the table after metadata item selection
            treeCtrl.getTree().getSelectionModel().selectedItemProperty()
                    .addListener((observable, oldVal, selectedItem) ->
                                    tableCtrl.fillItems(selectedItem == null ? null : selectedItem.getValue())
                    );

            // TODO find a nicer way to couple tree and table

            // TODO add listener for tree destruction - items clean up?

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
