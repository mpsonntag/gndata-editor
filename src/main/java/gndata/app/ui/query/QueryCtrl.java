// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.query;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.inject.Inject;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import gndata.app.state.MetadataState;
import gndata.app.ui.metadata.table.*;


/**
 * Controller for the main application window.
 */
public class QueryCtrl implements Initializable {

    @FXML
    public BorderPane queryView;
    @FXML
    private SplitPane splitPane;
    @FXML
    private GridPane queryPane;
    @FXML
    private ListView listPane;
    @FXML
    private TextArea renderedQuery;

    private RDFTableView tableView;

    @Inject
    public QueryCtrl(MetadataState metadataState, RDFTableView tableView) {
        this.tableView = tableView;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            splitPane.getItems().add(tableView.getScene());

            int numRows = queryPane.getChildren().size();

            TextField to = new TextField();
            TextField tp = new TextField();
            TextField ts = new TextField();
            Button addMore = new Button("+");

            GridPane.setConstraints(to, 0, numRows);
            GridPane.setConstraints(tp, 1, numRows);
            GridPane.setConstraints(ts, 2, numRows);
            GridPane.setConstraints(addMore, 3, numRows);

            queryPane.getChildren().addAll(to, tp, ts, addMore);

            addMore.setOnAction(e -> {
                int index = (int) addMore.getProperties().get("gridpane-row");

                List<Node> toRemove = new ArrayList<>();
                queryPane.getChildren().forEach(node -> {
                    if (GridPane.getRowIndex(node) == index) {
                        System.out.println(GridPane.getRowIndex(node).toString());
                        toRemove.add(node);
                    }
                });

                queryPane.getChildren().removeAll(toRemove);
            });
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
