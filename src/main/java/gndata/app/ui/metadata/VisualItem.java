// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.metadata;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;

/**
 * Class to render RDF items.
 */
public class VisualItem {

    public static String getClassName(Resource node) {
        String name = null;

        if (node.listProperties(RDF.type).hasNext()) {
            Resource cls = node.listProperties(RDF.type)
                    .nextStatement()
                    .getObject()
                    .asResource();

            if (!cls.equals(OWL.Class)) {
                name = cls.getLocalName();
            }
        }

        return name;
    }

    public static String getLabel(Resource node) {
        String name = null;

        if (node.listProperties(RDFS.label).hasNext()) {
            name = node.listProperties(RDFS.label)
                    .nextStatement()
                    .getObject()
                    .toString();
        }

        return name;
    }

    public static String getID(Resource node) {
        return node.getLocalName();
    }

    public static String renderForSorting(RDFNode item) {

        if (!item.isResource())
            return item.toString();

        Resource node = item.asResource();

        String classname = VisualItem.getClassName(node);
        String name = "";

        if (classname == null) { // root nodes
            name = VisualItem.getID(node);
        } else { // non-root nodes
            String label = VisualItem.getLabel(node);

            name = String.format("%s: %s", classname, label == null ? "" : label);
        }

        return name;
    }
}
