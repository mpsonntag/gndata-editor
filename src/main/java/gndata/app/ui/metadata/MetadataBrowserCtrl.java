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

import gndata.app.ui.metadata.table.RDFTableView;

/**
 * Controller for the main application window.
 */
public class MetadataBrowserCtrl implements Initializable {

    @FXML
    public BorderPane view;
    @FXML
    private SplitPane splitPane;

    private final MetadataNavView navView;
    private final MetadataFavoritesView favoritesView;
    private final MetadataListView listView;
    private final RDFTableView tableView;

    @Inject
    public MetadataBrowserCtrl(MetadataNavView navView, MetadataFavoritesView favoritesView,
                               MetadataListView listView, RDFTableView tableView) {
        this.navView = navView;
        this.favoritesView = favoritesView;
        this.listView = listView;
        this.tableView = tableView;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            view.setTop(navView.getScene());
            view.setLeft(favoritesView.getScene());
            // split pane with metadata tree
            splitPane.getItems().add(listView.getScene());
            splitPane.getItems().add(tableView.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
