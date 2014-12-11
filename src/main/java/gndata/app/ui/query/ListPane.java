// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.query;

import javafx.collections.*;
import javafx.scene.control.ListView;

import com.hp.hpl.jena.rdf.model.*;
import gndata.app.state.QueryState;
import gndata.app.ui.util.TwoLineListCell;
import gndata.lib.util.Resources;


public class ListPane extends ListView<Statement> {

    public ListPane(QueryState qs) {
        super();

        setCellFactory(view -> new QueryListCell());

        // update State when item is selected
        getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) ->
            qs.setSelectedStatement(newVal != null ? newVal : null)
        );

        // update list contents if the selection changed
        qs.getSelectedModel().addListener((obs, odlVal, newVal) ->
                updateSelection(qs.getSelectedModel().get()));
    }

    private void updateSelection(Model selection) {
        ObservableList<Statement> lst = FXCollections.observableArrayList();
        if (selection != null) {
            lst.setAll(selection.listStatements().toList());
        }

        getItems().setAll(lst);
    }

    private class QueryListCell extends TwoLineListCell<Statement> {

        @Override
        protected void update(Statement stmt, boolean empty) {
            if (!empty) {
                Resource node = stmt.getSubject();

                lineOne.set(Resources.toNameString(node));
                lineTwo.set(Resources.toInfoString(node));
            }
        }
    }
}
