// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util;

import javafx.scene.control.*;
import javafx.util.Callback;

import gndata.lib.srv.ResourceAdapter;

/**
 * Provides the nameString of a {@link ResourceAdapter} for display in a {@link ListView} {@link ListCell}.
 */
public class ObjectPropertyListItemCellFactory implements Callback<ListView<ResourceAdapter>, ListCell<ResourceAdapter>> {

    @Override
    public ListCell<ResourceAdapter> call(ListView<ResourceAdapter> p) {
        return new ObjectPropertyCell();
    }

    private class ObjectPropertyCell extends ListCell<ResourceAdapter> {
        @Override
        public void updateItem(ResourceAdapter item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty && item != null) {
                setText(item.toNameString());
            }
        }
    }
}
