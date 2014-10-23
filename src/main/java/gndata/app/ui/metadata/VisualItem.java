package gndata.app.ui.metadata;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;

/**
 * Class to render RDF items.
 */
public class VisualItem {

    public static String renderResource(RDFNode item) {

        if (item == null) { return ""; }

        if (!item.isResource()) { return item.toString(); }

        Resource node = item.asResource();
        String name = "";

        // "<resource_name>" as label if exists, id otherwise
        if (node.listProperties(RDFS.label).hasNext()) {
            name += node.listProperties(RDFS.label)
                    .nextStatement()
                    .getObject()
                    .toString();
        } else {
            name += node.getLocalName();
        }

        // builds "<class_name>: <resource_name>"
        if (node.listProperties(RDF.type).hasNext()) {
            Resource cls = node.listProperties(RDF.type)
                    .nextStatement()
                    .getObject()
                    .asResource();

            if (!cls.equals(OWL.Class)) {
                String prefix = cls.getLocalName() + ": ";
                return name.length() < 15 ? prefix + name : prefix + name.substring(0, 14);
            }
        }

        return name;
    }
}
