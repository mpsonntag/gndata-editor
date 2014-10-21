package gndata.app.ui.metadata.tree;

import javafx.scene.control.TreeCell;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;

/**
 * Facade class for metadata tree items.
 */
public final class TreeDisplay extends TreeCell<RDFNode> {

    public TreeDisplay() {}

    @Override
    public void updateItem(RDFNode item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
        } else {
            setText(renderResource(item));
        }
    }

    private String renderResource(RDFNode item) {

        if (item == null) { return ""; }

        if (!item.isResource()) { return item.toString(); }

        Resource node = item.asResource();
        String name = node.getLocalName();

        // add Class name to the resource representation
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