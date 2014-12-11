// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.query;

import java.util.*;
import java.util.stream.Collectors;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import gndata.app.state.QueryState;
import gndata.app.ui.util.StatementTableItem;
import gndata.lib.util.Resources;


public class TablePane extends TableView<StatementTableItem> {

    private ObservableList<StatementTableItem> statements;

    public TablePane(QueryState qs) {
        super();

        this.statements = FXCollections.observableList(new ArrayList<StatementTableItem>());

        TableColumn<StatementTableItem,String> c1 = new TableColumn<>("Predicate");
        c1.setCellValueFactory(new PropertyValueFactory("predicate"));
        TableColumn<StatementTableItem,String> c2 = new TableColumn<>("Literal Value");
        c2.setCellValueFactory(new PropertyValueFactory("literal"));
        getColumns().setAll(c1, c2);

        qs.getSelectedStatement().addListener((obs, odlVal, newVal) -> {
            if (newVal == null)
                statements.clear();
            else {
                List<StatementTableItem> lst = new ArrayList<>();

                lst = Resources.streamLiteralsFor(newVal.getSubject())
                        .map(StatementTableItem::new)
                        .collect(Collectors.toList());

                statements.setAll(lst);
            }
        });

        setItems(statements);
    }
}
