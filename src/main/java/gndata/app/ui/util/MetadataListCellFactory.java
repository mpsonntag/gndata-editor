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

import gndata.lib.srv.*;

/**
 * A list cell factory providing a cell for lists showing a {@link ResourceFileAdapter}.
 */
public class MetadataListCellFactory implements Callback<ListView<ResourceFileAdapter>, ListCell<ResourceFileAdapter>> {

    @Override
    public ListCell<ResourceFileAdapter> call(ListView<ResourceFileAdapter> p) {
        return new MetadataListCell();
    }

    private class MetadataListCell extends TwoLineListCell<ResourceFileAdapter> {
        @Override
        protected void update(ResourceFileAdapter item, boolean empty) {
            if (! empty) {
                lineOne.set(item.getFileName());
                lineTwo.set(item.toInfoString());
            } else {
                lineOne.set(null);
                lineTwo.set(null);
            }
        }
    }
}
