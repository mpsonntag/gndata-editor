// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.metadata.tree;

import javafx.scene.control.TreeCell;

import com.hp.hpl.jena.rdf.model.RDFNode;
import gndata.app.ui.metadata.VisualItem;

/**
 * Facade class for metadata tree items.
 */
public final class RDFTreeCell extends TreeCell<RDFNode> {

    @Override
    public void updateItem(RDFNode item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
        } else {
            setText(VisualItem.renderResource(item));
        }
    }
}
