package gndata.app.ui.util;

import com.hp.hpl.jena.rdf.model.*;
import javafx.collections.FXCollections;
import javafx.scene.control.TreeItem;
import javafx.collections.ObservableList;


/**
 * A tree representation of the RDF model graph.
 */
public class RDFTreeItem extends TreeItem<Resource> {

    private Model model;
    private Resource resource;

    private Property subClassOf;
    private Property isType;

    /**
     * Builds a new TreeItem based on a given RDF Resource
     *
     * @param mod   RDF model
     * @param res   an actual RDF Resource that a current TreeItem represents
     */
    public RDFTreeItem(Model mod, Resource res) {
        super(res);

        model = mod;
        resource = res;

        String pURI = "http://www.w3.org/2000/01/rdf-schema#subClassOf";
        subClassOf = model.getProperty(pURI);

        String tURI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
        isType = model.getProperty(tURI);
    }

    /**
     * Builds children items of this TreeItem node. List of children contains
     * - subclasses of an actual Resource if it is a Class
     * - actual members of a class if it is a Class
     * - actual resource properties, except parent (of course)
     *
     * @return  observable list of TreeItem nodes
     */
    @Override public ObservableList<TreeItem<Resource>> getChildren() {
        ObservableList<TreeItem<Resource>> children = FXCollections.observableArrayList();

        // OWL class hierarchy
        StmtIterator iterH = model.listStatements(null, subClassOf, resource);
        while (iterH.hasNext()) {
            Statement st = iterH.nextStatement();
            children.add(new RDFTreeItem(model, st.getSubject()));
        }

        // actual members of a class
        StmtIterator iterM = model.listStatements(null, isType, resource);
        while (iterM.hasNext()) {
            Statement st = iterM.nextStatement();
            children.add(new RDFTreeItem(model, st.getSubject()));
        }

        // properties of a current resource
        StmtIterator iterP = resource.listProperties();
        while (iterP.hasNext()) {
            Statement st = iterP.nextStatement();

            Property predicate = st.getPredicate();
            RDFNode obj = st.getObject();

            // exclude Literals and Class definitions
            if (obj.isResource() && !predicate.equals(isType)) {

                // exclude Parent
                TreeItem<Resource> parent = getParent();
                if (parent != null && !parent
                        .getValue()
                        .toString()
                        .equals(resource.toString())) {
                    children.add(new RDFTreeItem(model, obj.asResource()));
                }
            }
        }

        super.getChildren().setAll(children);

        return super.getChildren();
    }

    @Override public boolean isLeaf() {
        return getChildren().size() == 0;
    }
}
