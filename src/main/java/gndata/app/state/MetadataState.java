// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.state;

import javax.inject.Singleton;
import javafx.beans.property.*;

import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * Class that provides state information about metadata selections, changes and
 * other related information.
 */
@Singleton
public class MetadataState {

    private ObjectProperty<RDFNode> selectedNode;

    public MetadataState() {
        selectedNode = new SimpleObjectProperty<>();
    }

    public RDFNode getSelectedNode() {
        return selectedNode.get();
    }

    public ObjectProperty<RDFNode> selectedNodeProperty() {
        return selectedNode;
    }

    public void setSelectedNode(RDFNode selectedNode) {
        this.selectedNode.set(selectedNode);
    }
}
