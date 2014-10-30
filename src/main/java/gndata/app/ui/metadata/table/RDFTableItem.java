// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.metadata.table;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.rdf.model.*;

/**
 * Class that implements rendering of table items with RDF literals.
 */
public class RDFTableItem {

    private final Property predicate;
    private final Literal literal;

    public RDFTableItem(Property predicate, Literal literal) {
        this.predicate = predicate;
        this.literal = literal;
    }

    public String getPredicate() {
        return predicate.getLocalName();
    }

    public String getLiteral() {
        return literal.getValue().toString();
    }

    public String getType() {
        RDFDatatype dt = literal.getDatatype();

        return dt != null ? dt.getJavaClass().getSimpleName() : "";
    }
}
