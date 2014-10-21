package gndata.app.ui.metadata.tree;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;


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
     * Defines whether this TreeItem represents a Resource or a Literal.
     *
     * @return  is actual node a Literal.
     */
    public boolean isLiteralNode() {
        return node.isLiteral();
    }

    /**
     * Builds children items of this TreeItem node. List of children contains
     * - subclasses of an actual resource if it is a Class
     * - actual members of a class if it is a Class
     * - actual resource properties, except parent (of course)
     *
     * @return  observable list of TreeItem nodes
     */
    private ObservableList<RDFTreeItem> buildChildren() {
        ObservableList<RDFTreeItem> children = FXCollections.observableArrayList();

        if (!node.isResource()) return children;

        Resource r = node.asResource();
        Model m = node.getModel();

        // properties and related objects of a current resource
        StmtIterator iter = r.listProperties();
        while (iter.hasNext()) {
            Statement st = iter.nextStatement();

            Property p = st.getPredicate();
            RDFNode obj = st.getObject();

            // exclude parent, type, subclass and Thing triples
            // exclude Literals
            if (!obj.equals(getParent().getValue()) && !obj.equals(OWL.Thing)
                    && !p.equals(RDF.type) && !p.equals(RDFS.subClassOf) &&
                    !obj.isLiteral()) {

                children.add(new RDFTreeItem(obj));
            }
        }

        // actual members of a class, for the top tree level
        iter = m.listStatements(null, RDF.type, r);
        if (iter.hasNext()) {
            while (iter.hasNext()) {
                Statement st = iter.nextStatement();
                children.add(new RDFTreeItem(st.getSubject()));
            }
        } else { // reverse relationships
            iter = m.listStatements(null, null, r);
            while (iter.hasNext()) {
                Statement st = iter.nextStatement();

                Resource subj = st.getSubject();
                Property p = st.getPredicate();
                RDFNode obj = st.getObject();

                boolean is_equal_to_parent = getParent() != null &&
                        getParent().getValue().isResource() &&
                        subj.equals(getParent().getValue().asResource());

                boolean is_ontology_related = obj.equals(OWL.Thing) ||
                        p.equals(RDF.type) ||
                        p.equals(RDFS.subClassOf);

                if (!is_equal_to_parent && !is_ontology_related) {
                    children.add(new RDFTreeItem(subj));
                }
            }
        }

        // sort alphabetically + separate related objects and literals
        children.sort((a, b) -> a.toString().compareTo(b.toString()));
        children.sort((a, b) ->
                !a.isLiteralNode() && b.isLiteralNode() ? -1 :
                        a.isLiteralNode() && b.isLiteralNode() ? 0 : 1);

        return children;
    }
}
