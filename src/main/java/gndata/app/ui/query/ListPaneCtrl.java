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
import javafx.collections.FXCollections;
import javafx.fxml.*;
import javafx.scene.control.ListView;

import com.google.inject.Inject;
import com.hp.hpl.jena.rdf.model.Statement;
import gndata.app.state.QueryState;
import gndata.app.ui.util.TwoLineListCell;
import gndata.lib.srv.ResourceAdapter;


public class ListPaneCtrl implements Initializable {

    @FXML
    private ListView<Statement> lv;

    private QueryState qs;

    @Inject
    public ListPaneCtrl(QueryState qs) {
        this.qs = qs;

        qs.selectedModelProperty().addListener((o, p, n) ->
            lv.getItems().setAll(n == null ? FXCollections.observableArrayList() : n.listStatements().toList())
        );
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lv.setCellFactory(view -> new QueryListCell());

        lv.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, newVal) ->
                        qs.setSelectedStatement(newVal != null ? newVal : null)
        );
    }

    private class QueryListCell extends TwoLineListCell<Statement> {

        @Override
        protected void update(Statement stmt, boolean empty) {
            if (!empty) {
                ResourceAdapter node = new ResourceAdapter(stmt.getSubject());

                lineOne.set(node.toNameString());
                lineTwo.set(node.toNameString());
            }
        }
    }
}
