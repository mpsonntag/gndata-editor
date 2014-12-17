// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.metadata;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.TableView;
import javafx.scene.web.WebView;

import gndata.app.html.PageCtrl;
import gndata.app.state.MetadataNavState;
import gndata.app.ui.util.*;
import gndata.lib.util.Resources;

/**
 * Controller for the table to view metadata items.
 */
public class MetadataDetailsCtrl extends PageCtrl {

    @FXML
    private TogglePane togglePane;
    @FXML
    private WebView webView;
    @FXML
    private TableView<StatementTableItem> tableView;

    private MetadataNavState metadataState;
    private ObservableList<StatementTableItem> statements;

    @Inject
    public MetadataDetailsCtrl(MetadataNavState metadataState) {
        this.metadataState = metadataState;
        this.statements = FXCollections.observableList(new ArrayList<StatementTableItem>());

        metadataState.selectedNodeProperty().addListener((obs, odlVal, newVal) -> {
            getPage().applyModel(newVal);

            if (newVal == null) {
                statements.clear();
            } else {
                statements.setAll(
                        Resources.streamLiteralsFor(newVal.getResource())
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
