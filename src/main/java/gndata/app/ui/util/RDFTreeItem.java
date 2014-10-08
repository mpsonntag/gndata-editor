package gndata.app.ui.util;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import javafx.collections.FXCollections;
import javafx.scene.control.TreeItem;
import javafx.collections.ObservableList;


/**
 * A tree representation of the RDF model graph.
 */
public class RDFTreeItem extends TreeItem<Resource> {

    private Model model;
    private Resource resource;

    /**
     * Builds a new TreeItem based on a given RDF Resource
     *
     * @param mod   RDF model to query for children
     * @param res   an actual RDF Resource that a current TreeItem represents
     */
    public RDFTreeItem(Model mod, Resource res) {
        super(res);

        model = mod;
        resource = res;
    }

    public static ObservableList<TreeItem<Resource>> getRootClasses(OntModel schema) {
        ObservableList<TreeItem<Resource>> items = FXCollections.observableArrayList();

        StmtIterator iterH = schema.listStatements(null, RDFS.subClassOf, OWL.Thing);
        while (iterH.hasNext()) {
            Statement st = iterH.nextStatement();
            items.add(new RDFTreeItem(schema, st.getSubject()));
        }

        return items;
    }

    public static ObservableList<TreeItem<Resource>> getRootItems(Model model) {
        ObservableList<TreeItem<Resource>> items = FXCollections.observableArrayList();

        StmtIterator iterH = model.listStatements(null, RDFS.subClassOf, OWL.Thing);
        while (iterH.hasNext()) {
            Statement st = iterH.nextStatement();
            items.add(new RDFTreeItem(model, st.getSubject()));
        }

        return items;
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
        StmtIterator iterH = model.listStatements(null, RDFS.subClassOf, resource);
        while (iterH.hasNext()) {
            Statement st = iterH.nextStatement();
            children.add(new RDFTreeItem(model, st.getSubject()));
        }

        // actual members of a class
        StmtIterator iterM = model.listStatements(null, RDF.type, resource);
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
            if (obj.isResource() && !obj.equals(OWL.Thing) && !predicate.equals(RDF.type)) {

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
