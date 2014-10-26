package gndata.app.ui.metadata.tree;

import javafx.scene.control.TreeCell;

import com.hp.hpl.jena.rdf.model.RDFNode;
import gndata.app.ui.metadata.VisualItem;

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
            setText(VisualItem.renderResource(item));
        }
    }
}