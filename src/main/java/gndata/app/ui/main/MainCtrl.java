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
import javax.management.Query;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import gndata.app.ui.filebrowser.FileBrowserView;
import gndata.app.ui.metadata.MainMetadataView;
import gndata.app.ui.metadata.table.RDFTableView;
import gndata.app.ui.metadata.tree.RDFTreeView;
import gndata.app.ui.query.QueryView;

/**
 * Controller for the main application window.
 */
public class MainCtrl implements Initializable {

    @FXML
    public BorderPane view;
    @FXML
    public Tab dashboard;
    @FXML
    public Tab metadata;
    @FXML
    public Tab calendar;
    @FXML
    public Tab query;
    @FXML
    public Tab files;
    @FXML
    public Tab notes;


    private MenuView menuView;

    private MainMetadataView metadataView;

    private QueryView queryView;

    private FileBrowserView fileBrowserView;

    @Inject
    public MainCtrl(MenuView menuView, MainMetadataView metadataView, QueryView queryView, FileBrowserView fileBrowserView) {
        this.menuView = menuView;
        this.metadataView = metadataView;
        this.queryView = queryView;
        this.fileBrowserView = fileBrowserView;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // top Menu bar
            view.setTop(menuView.getScene());

            metadata.setContent(metadataView.getScene());
            query.setContent(queryView.getScene());

            files.setContent(fileBrowserView.getScene());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
