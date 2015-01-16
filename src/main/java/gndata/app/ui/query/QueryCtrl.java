// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.query;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.*;
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
    private TextArea prefixArea;
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

        projectState.configProperty().addListener((o, p, n) -> {
            if (projectState.isConfigured()) {
                prefixArea.setText(projectState.getMetadata().getPrefixHeader());
            }
        });
    }

    public Model runQuery() {
        String maxResults = "100";

        Model selection = ModelFactory.createDefaultModel();

        if (projectState.isConfigured()) {
            try {
                ResultSet results = projectState.getMetadata().SELECT(
                    StrUtils.strjoinNL(
                        projectState.getMetadata().getPrefixHeader(),
                        queryState.getCurrentQuery(),
                        "LIMIT " + maxResults
                    ));


                List<Statement> lst = new ArrayList<>();

                // this simply parses the result set and creates a model
                // containing all the statements from it
                results.forEachRemaining(soln -> {
                    results.getResultVars().forEach(var -> {
                        RDFNode n = soln.get(var);

                        if (n.isResource()) {
                            StmtIterator iter = projectState.getMetadata()
                                    .getAnnotations()
                                    .listStatements((Resource) n, null, (RDFNode) null);

                            if (iter.hasNext()) {
                                lst.addAll(iter.toList());
                            }
                        }
                    });
                });

                selection = selection.add(lst);

            } catch (QueryParseException e) {
                ta.setText(e.getMessage());
            }
        }

        return selection;
    }
}
