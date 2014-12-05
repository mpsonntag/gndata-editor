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
    private VBox vBox;
    @FXML
    private Tab textLikeView;
    @FXML
    private Tab tableLikeView;

    private ProjectState projectState;
    private QueryState queryState;

    private TextArea ta;

    @Inject
    public QueryCtrl(ProjectState ps, QueryState qs) {
        this.projectState = ps;
        this.queryState = qs;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        queryState.getCurrentQuery().addListener((obs, odlVal, newVal) ->
                queryState.setSelectedModel(runQuery()));

        QueryPane qp = new QueryPane(queryState);

        ListPane lp = new ListPane(queryState);
        VBox.setVgrow(lp, Priority.ALWAYS);

        ta = new TextArea();
        ta.setEditable(false);
        ta.textProperty().bindBidirectional(queryState.getCurrentQuery());

        vBox.getChildren().addAll(qp, lp, ta);

        tableLikeView.setContent(new TablePane(queryState));
        textLikeView.setContent(TextPane.getInstance(queryState));
    }

    public Model runQuery() {
        String maxResults = "100";

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
