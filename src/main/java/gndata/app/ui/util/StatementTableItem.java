// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util;

import java.util.*;
import java.util.stream.Collectors;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.rdf.model.*;
import gndata.lib.util.Resources;

/**
 * Class that implements rendering of table items with RDF literals.
 */
public class StatementTableItem {

    private final Statement statement;

    public StatementTableItem(Statement statement) {
        this.statement = statement;
    }

    public String getPredicate() {
        return statement.getPredicate().getLocalName();
    }

    public String getLiteral() {
        RDFNode object = statement.getObject();
        if (object.isLiteral()) {
            return object.asLiteral().getValue().toString();
        } else {
            return "";
        }
    }

    public String getType() {
        RDFNode object = statement.getObject();
        if (object.isLiteral()) {
            RDFDatatype dt = object.asLiteral().getDatatype();
            return dt != null ? dt.getJavaClass().getSimpleName() : "String";
        } else {
            return "";
        }
    }

    @Deprecated // because can be easily replaced using Resources.streamLiteralsFor
    public static List<StatementTableItem> buildTableItems(RDFNode node) {
        if (node == null || !node.isResource())
            return new ArrayList<>();

        return Resources.streamLiteralsFor(node.asResource())
                        .map(StatementTableItem::new)
                        .collect(Collectors.toList());

    }
}
