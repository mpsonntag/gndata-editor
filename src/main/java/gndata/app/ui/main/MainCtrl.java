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

import gndata.app.ui.metadata.table.TableView;
import gndata.app.ui.metadata.tree.TreeView;

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
    private TreeView metadataView;
    @Inject
    private TableView tableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // top Menu bar
            view.setTop(menuView.getScene());

            // split pane with metadata tree
            splitPane.getItems().add(metadataView.getScene());
            splitPane.getItems().add(tableView.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
