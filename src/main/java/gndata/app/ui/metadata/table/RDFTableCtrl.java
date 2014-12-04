// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.metadata.table;

import java.net.URL;
import java.util.*;
import javax.inject.Inject;
import javafx.collections.*;
import javafx.collections.transformation.SortedList;
import javafx.fxml.*;
import javafx.scene.control.TableView;

import com.hp.hpl.jena.rdf.model.*;
import gndata.app.state.MetadataState;
import gndata.app.ui.util.RDFTableItem;

/**
 * Controller for the table to view metadata items.
 */
public class RDFTableCtrl implements Initializable {

    private MetadataState metadataState;

    @FXML
    private TableView<RDFTableItem> tableView;

    @Inject
    public RDFTableCtrl(MetadataState metadataState) {
        this.metadataState = metadataState;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // listen to changes in metadata state
        metadataState.selectedNodeProperty().addListener((obs, odlVal, newVal) -> fillItems(newVal));
    }

    public void fillItems(RDFNode node) {
        List<RDFTableItem> items = RDFTableItem.buildTableItems(node);

        if (items.size() > 0) {
            items.sort((a, b) -> a.getPredicate().compareTo(b.getPredicate()));

            ObservableList<RDFTableItem> observableData = FXCollections.observableArrayList(items);
            SortedList<RDFTableItem> sortedData = new SortedList<>(observableData);

            // sort items by predicate value
            sortedData.setComparator((a, b) -> a.getPredicate().compareTo(b.getPredicate()));
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());

            tableView.setItems(sortedData);
        } else {
            tableView.setItems(null);
        }
    }
}
