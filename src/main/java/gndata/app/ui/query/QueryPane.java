// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.query;

import java.util.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import gndata.app.state.QueryState;
import org.apache.jena.atlas.lib.StrUtils;

/**
 * An extension of the grid pane that can add/remove text fields.
 */
public class QueryPane extends GridPane {

    private Button addMore;
    public Button submit;

    public QueryPane(QueryState qs) {
        super();

        addMore = new Button("+");
        addMore.setOnAction(e -> addEmptyQueryRow());
        GridPane.setConstraints(addMore, 3, 0);

        submit = new Button("Submit");
        GridPane.setConstraints(submit, 0, 0);

        getChildren().addAll(submit, addMore);

        addQueryRow("?x", "?y", "?z");

        submit.setOnAction(e -> qs.setCurrentQuery(readQuery()));
    }

    private void addEmptyQueryRow() {
        addQueryRow("", "", "");
    }

    private void addQueryRow(String subj, String pred, String obj) {
        int rowIndex = GridPane.getRowIndex(addMore);

        GridPane.setRowIndex(addMore, rowIndex + 1);
        GridPane.setRowIndex(submit, rowIndex + 1);

        TextField to = new TextField(subj);
        TextField tp = new TextField(pred);
        TextField ts = new TextField(obj);
        Button remRow = new Button("-");

        GridPane.setConstraints(to, 0, rowIndex);
        GridPane.setConstraints(tp, 1, rowIndex);
        GridPane.setConstraints(ts, 2, rowIndex);
        GridPane.setConstraints(remRow, 3, rowIndex);

        getChildren().addAll(to, tp, ts, remRow);

        remRow.setOnAction(e -> {
            int index = (int) remRow.getProperties().get("gridpane-row");

            List<Node> toRemove = new ArrayList<>();
            getChildren().forEach(node -> {
                if (GridPane.getRowIndex(node) == index &&
                        getChildren().size() > 5) {
                    toRemove.add(node);
                }
            });

            getChildren().removeAll(toRemove);
        });
    }

    /**
     * Iterates over all text input fields and reads actual inputs.
     *
     * @return  list of inputs
     */
    private List<String> getTextInputs() {
        List<String> lst = new ArrayList<>();

        getChildren().forEach(node -> {
            if (node instanceof TextField) {
                TextField field = (TextField) node;
                lst.add(field.getText());
            }
        });

        return lst;
    }

    /**
     * Assembles the query body from several input fields.
     *
     * @return  SPARQL query body
     */
    public String readQuery() {
        List<String> lst = getTextInputs();

        Set<String> selectors = new LinkedHashSet<>();
        lst.forEach(input -> {
            if (input.length() > 0 && input.substring(0, 1).equals("?")) {
                selectors.add(input);
            }
        });

        List<String> conditions = new ArrayList<>();
        for(int i = 0; i < lst.size()/3; i++) {
            conditions.add(StrUtils.strjoin(" ",
                lst.get(i * 3), lst.get(i * 3 + 1), lst.get(i * 3 + 2)
            ));
        }

        return StrUtils.strjoinNL(
                "CONSTRUCT {",
                String.join(" ", selectors),
                "} WHERE { ",
                String.join(" . ", conditions),
                "}"
        );
    }
}
