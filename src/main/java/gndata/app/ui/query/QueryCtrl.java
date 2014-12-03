// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.query;

import gndata.app.ui.metadata.table.RDFTableView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Controller for the Query application tab.
 */
public class QueryCtrl implements Initializable {

    @FXML
    public BorderPane queryView;
    @FXML
    private SplitPane splitPane;
    @FXML
    private VBox vBox;

    private RDFTableView tableView;

    @Inject
    public QueryCtrl(RDFTableView tableView) {
        // TODO create a separate instance of table view
        this.tableView = tableView;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            QueryPane qp = new QueryPane();

            ListView lw = new ListView();
            VBox.setVgrow(lw, Priority.ALWAYS);

            TextArea ta = new TextArea();

            vBox.getChildren().addAll(qp, lw, ta);
            splitPane.getItems().add(tableView.getScene());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
