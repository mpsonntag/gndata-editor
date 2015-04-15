// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.rdf.model.*;
import gndata.lib.srv.StatementAdapter;
import gndata.lib.srv.StatementAdapter.Action;

/**
 * Class that implements rendering of table items with RDF literals.
 */
public class DataPropertyTableItem {

    private StatementAdapter saItem;

    public DataPropertyTableItem(Statement statement) {
        this.saItem = new StatementAdapter(statement);
    }

    public String getPredicate() {
        return saItem.getOriginalStatement().getPredicate().getLocalName();
    }

    public String getLiteral() {
        RDFNode object = saItem.getModifiedObject();
        if (object.isLiteral()) {
            return object.asLiteral().getValue().toString();
        } else {
            return "";
        }
    }

    public DataPropertyTableItem setLiteral(String oldVal, String newVal) {
        if(!oldVal.equals(newVal)) {
            this.saItem.setModifiedObject(ResourceFactory.createPlainLiteral(newVal));
            this.saItem.setAction(Action.UPDATE);
        }
        return this;
    }

    public String getType() {
        RDFNode object = saItem.getOriginalStatement().getObject();
        if (object.isLiteral()) {
            RDFDatatype dt = object.asLiteral().getDatatype();
            return dt != null ? dt.getJavaClass().getSimpleName() : "String";
        } else {
            return "";
        }
    }

    // TODO return proper icon
    public String getIcon() {
        return "x";
    }

    public StatementAdapter getStatementAdapter(){
        return this.saItem;
    }
}
