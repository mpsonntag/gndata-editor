// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.metadata.table;

import java.util.*;
import javafx.collections.*;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Controller for the table to view metadata items.
 */
public class TableCtrl {

    @FXML
    private TableView<TableItem> tableView;

    public void fillItems(RDFNode node) {
        List<TableItem> items = buildTableItems(node);

        if (items.size() > 0) {
            items.sort((a, b) -> a.getPredicate().compareTo(b.getPredicate()));

            ObservableList<TableItem> observableData = FXCollections.observableArrayList(items);
            SortedList<TableItem> sortedData = new SortedList<>(observableData);

            // sort items by predicate value
            sortedData.setComparator((a, b) -> a.getPredicate().compareTo(b.getPredicate()));
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());

            tableView.setItems(sortedData);
        } else {
            tableView.setItems(null);
        }
    }

    public static List<TableItem> buildTableItems(RDFNode node) {
        List<TableItem> items = new ArrayList<>();

        if (node == null || !node.isResource()) { return items; }

        Resource r = node.asResource();
        StmtIterator iter = r.listProperties();
        while (iter.hasNext()) {
            Statement st = iter.nextStatement();

            if (st.getObject().isLiteral()) {
                Property p = st.getPredicate();
                Literal l = st.getObject().asLiteral();

                items.add(new TableItem(p, l));
            }
        }

        return items;
    }
}
