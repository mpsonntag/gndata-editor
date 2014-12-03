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
import javafx.collections.ObservableList;

import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * Class that provides state information about metadata selections, changes and
 * other related information.
 */
@Singleton
public class QueryState {

    private ObjectProperty<RDFNode> selectedNode;
    private ObjectProperty<String> currentQuery;

    private ObservableList<String> queryHistory;  // mockup for the future <-->

    public QueryState() {
        selectedNode = new SimpleObjectProperty<>();
        currentQuery = new SimpleObjectProperty<>();
    }

    public ObjectProperty<RDFNode> getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(RDFNode selectedNode) {
        this.selectedNode.set(selectedNode);
    }

    public ObjectProperty<String> getCurrentQuery() {
        return currentQuery;
    }

    public void setCurrentQuery(String query) {
        this.currentQuery.set(query);
    }
}
