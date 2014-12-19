// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.query.builder;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import gndata.lib.srv.ResourceEvent;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import gndata.app.state.QueryState;
import javafx.scene.layout.VBox;
import org.apache.jena.atlas.lib.StrUtils;

/**
 * An extension of the grid pane that can add/remove text fields.
 */
public class QueryPaneCtrl implements Initializable {

    @FXML
    private BorderPane queryPane;
    @FXML
    private ListView<QueryRow> lv;

    private ObservableList<QueryRow> queryRows;
    private QueryState qs;

    @Inject
    public QueryPaneCtrl(QueryState qs) {
        this.qs = qs;

        queryRows = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lv.setItems(queryRows);

        queryRows.add(new QueryRow("?x", "?y", "?z"));
    }

    public void addRow() {
        QueryRow qr = new QueryRow("", "", "");
        qr.onRemove(action -> queryRows.remove(qr));
        queryRows.add(qr);
    }

    public void submitQuery() {
        qs.setCurrentQuery(readQuery());
    }

    /**
     * Iterates over all text input fields and reads actual inputs.
     *
     * @return  list of inputs
     */
    private List<List<String>> getTextInputs() {
        List<List<String>> lst = new ArrayList<>();

        queryRows.forEach(row -> lst.add(row.getInput()));

        return lst;
    }

    /**
     * Assembles the query body from several input fields.
     *
     * @return  SPARQL query body
     */
    public String readQuery() {
        List<List<String>> lst = getTextInputs();

        Set<String> selectors = new LinkedHashSet<>();
        lst.forEach(row -> row.forEach(input -> {
                if (input.length() > 0 && input.substring(0, 1).equals("?")) {
                    selectors.add(input);
            }})
        );

        List<String> conditions = lst.stream()
                .map(row -> StrUtils.strjoin(" ", row))
                .collect(Collectors.toList());

        return StrUtils.strjoinNL(
                "CONSTRUCT {",
                String.join(" ", selectors),
                "} WHERE { ",
                String.join(" . ", conditions),
                "}"
        );
    }
}
