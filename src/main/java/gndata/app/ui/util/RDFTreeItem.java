package gndata.app.ui.util;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import javafx.collections.FXCollections;
import javafx.scene.control.TreeItem;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;


/**
 * A tree representation of the RDF model graph.
 */
public class RDFTreeItem extends TreeItem<RDFNode> {

    private RDFNode node;

    private boolean isLeaf;
    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;

    /**
     * Builds a new TreeItem based on a given RDF Node
     *
     * @param res   an actual RDF Node that a current TreeItem represents
     */
    public RDFTreeItem(RDFNode res) {
        super(res);
        node = res;
    }

    /**
     * Returns a list of top classes (direct subclass from OWL.Thing) from
     * a given ontology model.
     *
     * @param model     RDF Model with ontology terms
     * @return          list of TreeItem(s) representing top classes
     */
    public static ObservableList<TreeItem<RDFNode>> getRootClasses(Model model) {
        ObservableList<TreeItem<RDFNode>> items = FXCollections.observableArrayList();

        NodeIterator iter = model.listObjectsOfProperty(RDF.type);
        while (iter.hasNext()) {
            RDFNode st = iter.next();
            if (st.isResource()) {
                Resource r = st.asResource();

                // exclude OWL definitions from the root items
                if (!r.getNameSpace().equals("http://www.w3.org/2002/07/owl#")) {
                    items.add(new RDFTreeItem(r));
                }
            }
        }
        return items;
    }

    @Override public ObservableList<TreeItem<RDFNode>> getChildren() {
        if (isFirstTimeChildren) {
            isFirstTimeChildren = false;
            super.getChildren().setAll(buildChildren());
        }
        return super.getChildren();
    }

    @Override public boolean isLeaf() {
        if (isFirstTimeLeaf) {
            isFirstTimeLeaf = false;
            isLeaf = getChildren().size() == 0;
        }
        return isLeaf;
    }

    /**
     * Builds children items of this TreeItem node. List of children contains
     * - subclasses of an actual resource if it is a Class
     * - actual members of a class if it is a Class
     * - actual resource properties, except parent (of course)
     *
     * @return  observable list of TreeItem nodes
     */
    private ObservableList<TreeItem<RDFNode>> buildChildren() {
        ObservableList<TreeItem<RDFNode>> children = FXCollections.observableArrayList();

        if (node.isResource()) {
            Resource r = node.asResource();
            Model m = node.getModel();

            // properties of a current resource
            StmtIterator iter = r.listProperties();
            while (iter.hasNext()) {
                Statement st = iter.nextStatement();

                Property p = st.getPredicate();
                RDFNode obj = st.getObject();

                // exclude parent, type, subclass and Thing triples
                if (!obj.equals(getParent().getValue()) && !obj.equals(OWL.Thing)
                        && !p.equals(RDF.type) && !p.equals(RDFS.subClassOf)) {

                    children.add(new RDFTreeItem(obj));
                }
            }

            // actual members of a class
            iter = m.listStatements(null, RDF.type, r);
            while (iter.hasNext()) {
                Statement st = iter.nextStatement();
                children.add(new RDFTreeItem(st.getSubject()));
            }
        }

        return children;
    }
}
