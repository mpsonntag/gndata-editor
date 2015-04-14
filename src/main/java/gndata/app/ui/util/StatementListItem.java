// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util;

import com.hp.hpl.jena.rdf.model.*;


/**
 * Class that implements rendering of table items with RDF literals.
 */
public class StatementListItem {

    private final Statement originalStatement;
    private RDFNode modifiedObject;
    private String action;

    public final String add = "Add";
    public final String delete = "Delete";
    public final String update = "Update";

    public StatementListItem(Statement statement) {
        this.originalStatement = statement;
        this.modifiedObject = statement.getObject();
        this.action = this.update;
    }

    public Statement getOriginalStatement() {
        return this.originalStatement;
    }

    public RDFNode getModifiedObject() {
        return this.modifiedObject;
    }

    public void setModifiedObject(RDFNode r) {
        this.modifiedObject = r;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String s) {
        this.action = s;
    }
}
