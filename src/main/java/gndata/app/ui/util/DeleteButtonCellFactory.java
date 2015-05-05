// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.util.Callback;

/**
 * TableCell class required to delete existing DataProperties
 */

public class DeleteButtonCellFactory
        implements Callback<TableColumn<StatementTableItem, String>, TableCell<StatementTableItem, String>> {

    @Override
    public TableCell<StatementTableItem, String> call (TableColumn<StatementTableItem, String> p) {
        return new DeleteButtonCell();
    }

    // TODO pimp the appearance of the delete button
    private class DeleteButtonCell extends TableCell<StatementTableItem, String> {
        private final Button delButton = new Button("x");

        DeleteButtonCell() {
            delButton.setPadding(new Insets(1));
            delButton.setOnAction(event -> {
                // retrieve and remove corresponding StatementTableItem
                this.getTableView().getItems().remove(getTableRow().getIndex());
            });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                setGraphic(delButton);
            } else {
                setGraphic(null);
            }
        }
    }

}
