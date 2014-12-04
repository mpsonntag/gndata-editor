// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.query;

import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.rdf.model.Model;
import gndata.app.state.ProjectState;
import gndata.app.state.QueryState;
import gndata.app.ui.metadata.table.RDFTableView;
import gndata.lib.srv.MetadataService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.jena.atlas.lib.StrUtils;

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

    private String maxResults = "100";

    private ProjectState projectState;
    private QueryState queryState;

    private TablePane tb;
    private TextArea ta;

    @Inject
    public QueryCtrl(ProjectState ps, QueryState qs) {
        this.projectState = ps;
        this.queryState = qs;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        QueryPane qp = new QueryPane(queryState);

        queryState.getCurrentQuery().addListener((obs, odlVal, newVal) ->
                queryState.setSelectedModel(runQuery()));

        ListPane lp = new ListPane(queryState);
        VBox.setVgrow(lp, Priority.ALWAYS);

        ta = new TextArea();
        ta.setEditable(false);
        ta.textProperty().bindBidirectional(queryState.getCurrentQuery());

        vBox.getChildren().addAll(qp, lp, ta);

        tb = new TablePane(queryState);

        splitPane.getItems().add(tb);
    }

    public Model runQuery() {
        Model selection = null;

        if (projectState.isConfigured()) {
            try {
                selection = projectState.getMetadata().SELECT(
                    StrUtils.strjoinNL(
                        MetadataService.stdPrefix,
                        queryState.getCurrentQuery().get(),
                        "LIMIT " + maxResults
                    ));

            } catch (QueryParseException e) {
                ta.setText(e.getMessage());
            }
        }

        return selection;
    }
}
