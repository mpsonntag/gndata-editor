// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.query;

import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.rdf.model.Model;
import gndata.app.state.*;
import gndata.lib.srv.MetadataService;
import org.apache.jena.atlas.lib.StrUtils;


/**
 * Controller for the Query application tab.
 */
public class QueryCtrl implements Initializable {

    @FXML
    public BorderPane queryView;
    @FXML
    private TextArea ta;

    private ProjectState projectState;
    private QueryState queryState;

    @Inject
    public QueryCtrl(ProjectState ps, QueryState qs) {
        this.projectState = ps;
        this.queryState = qs;

        queryState.currentQueryProperty().addListener((obs, odlVal, newVal) ->
                queryState.setSelectedModel(runQuery()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ta.textProperty().bindBidirectional(queryState.currentQueryProperty());

        //tableLikeView.setContent(new TablePane(queryState));
        //textLikeView.setContent(TextPane.getInstance(queryState));
    }

    public Model runQuery() {
        String maxResults = "100";

        Model selection = null;

        if (projectState.isConfigured()) {
            try {
                selection = projectState.getMetadata().CONSTRUCT(
                    StrUtils.strjoinNL(
                        MetadataService.stdPrefix,
                        queryState.getCurrentQuery(),
                        "LIMIT " + maxResults
                    ));

            } catch (QueryParseException e) {
                ta.setText(e.getMessage());
            }
        }

        return selection;
    }
}
