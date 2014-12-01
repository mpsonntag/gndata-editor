// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.metadata;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javafx.fxml.*;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

import gndata.app.ui.main.MenuView;
import gndata.app.ui.metadata.table.RDFTableView;
import gndata.app.ui.metadata.tree.RDFTreeView;

/**
 * Controller for the main application window.
 */
public class MainMetadataCtrl implements Initializable {

    @FXML
    public BorderPane view;
    @FXML
    private SplitPane splitPane;

    private RDFTreeView metadataView;

    private RDFTableView tableView;

    @Inject
    public MainMetadataCtrl(RDFTreeView metadataView, RDFTableView tableView) {
        this.metadataView = metadataView;
        this.tableView = tableView;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // split pane with metadata tree
            splitPane.getItems().add(metadataView.getScene());
            splitPane.getItems().add(tableView.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
