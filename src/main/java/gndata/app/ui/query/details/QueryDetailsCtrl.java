// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.query.details;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.web.WebView;

import gndata.app.html.PageCtrl;
import gndata.app.state.*;
import gndata.app.ui.util.*;
import gndata.lib.srv.ResourceAdapter;
import gndata.lib.util.Resources;

/**
 * Controller for the table to view metadata items.
 */
public class QueryDetailsCtrl extends PageCtrl {

    @FXML
    private TogglePane togglePane;
    @FXML
    private WebView webView;
    @FXML
    private TableView<StatementTableItem> tableView;

    private QueryState qs;
    private ObservableList<StatementTableItem> statements;

    @Inject
    public QueryDetailsCtrl(QueryState qs) {
        this.qs = qs;
        this.statements = FXCollections.observableList(new ArrayList<StatementTableItem>());

        qs.selectedStatementProperty().addListener((obs, odlVal, newVal) -> {
            if (newVal != null) {
                getPage().applyModel(new ResourceAdapter(newVal.getSubject(), null));
            }

            if (newVal == null) {
                statements.clear();
            } else {
                statements.setAll(
                        Resources.streamLiteralsFor(newVal.getSubject())
                                .map(StatementTableItem::new)
                                .collect(Collectors.toList())
                );
            }
        });
    }

    @Override
    public WebView getWebView() {
        return webView;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        tableView.setItems(statements);
    }

}
