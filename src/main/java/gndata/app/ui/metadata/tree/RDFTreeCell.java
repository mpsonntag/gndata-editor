// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.metadata.tree;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

import com.hp.hpl.jena.rdf.model.*;
import gndata.app.ui.metadata.VisualItem;

/**
 * Facade class for metadata tree items.
 */
public final class RDFTreeCell extends TreeCell<RDFNode> {

    @Override
    public void updateItem(RDFNode item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || !item.isResource()) {
            setGraphic(new HBox());
        } else {
            Resource node = item.asResource();

            String classname = VisualItem.getClassName(node);
            String titletext, subtitletext = "";

            if (classname == null) { // root node
                titletext = VisualItem.getID(node);
            } else { // non-root node
                String label = VisualItem.getLabel(node);

                titletext = String.format("%s: %s", classname, label == null ? "" : label);
                subtitletext = VisualItem.getID(node);
            }

            Label title = new Label(titletext);
            title.setStyle("-fx-font-weight: bold;");

            Label subtitle = new Label(subtitletext);
            subtitle.setStyle("-fx-font-size: 0.7em; -fx-text-fill: grey;");

            HBox box = new HBox(title, subtitle);
            box.setAlignment(Pos.CENTER_LEFT);

            setGraphic(box);
        }
    }
}
