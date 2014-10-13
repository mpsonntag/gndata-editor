package gndata.app.ui.tree;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import javafx.scene.control.TreeCell;
import javafx.scene.paint.Color;

/**
 * Facade class for metadata tree items.
 */
public final class MetadataTreeCell extends TreeCell<RDFNode> {

    public MetadataTreeCell() {}

    @Override
    public void updateItem(RDFNode item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
        } else if (item != null) {

            if (item.isLiteral()) {
                setText(renderLiteral(item));
                setTextFill(Color.GREEN);
            } else {
                setText(renderResource(item));
                setTextFill(Color.BLACK);
            }
        }
    }

    private String renderLiteral(RDFNode item) {
        String text = "";

        // building a "<predicate>: <literal>" string
        RDFNode r = getTreeItem().getParent().getValue();
        StmtIterator iter = item.getModel().listStatements(r.asResource(), null, item);
        if (iter.hasNext()) {
            text += iter.nextStatement().getPredicate().getLocalName() + ": ";
        }

        return text + item.toString();
    }

    private String renderResource(RDFNode item) {
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
