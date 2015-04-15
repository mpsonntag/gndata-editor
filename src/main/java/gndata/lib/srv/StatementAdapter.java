// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.lib.srv;

import com.hp.hpl.jena.rdf.model.*;


/**
 * Class that provides a link from statements to UI components.
 */
public class StatementAdapter {

    private final Statement originalStatement;
    private RDFNode modifiedObject;
    private Action action;

    public StatementAdapter(Statement statement) {
        this.originalStatement = statement;
        this.modifiedObject = statement.getObject();
        this.action = Action.UPDATE;
    }

    public Statement getOriginalStatement() {
        return this.originalStatement;
    }

    public RDFNode getModifiedObject() {
        return this.modifiedObject;
    }

    public void setModifiedObject(RDFNode r) { this.modifiedObject = r; }

    public Action getAction() {
        return this.action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public enum Action {
        ADD, DELETE, UPDATE
    }
}
