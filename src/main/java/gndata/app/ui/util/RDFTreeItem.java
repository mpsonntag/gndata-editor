package gndata.app.ui.util;

import com.hp.hpl.jena.ontology.OntModel;
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
public class RDFTreeItem extends TreeItem<Resource> {

    private Model model;
    private Resource resource;

    private boolean isLeaf;
    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;

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

    /**
     * Returns a list of top classes (direct subclass from OWL.Thing) from
     * a given ontology model.
     *
     * @param schema    RDF Model with ontology terms
     * @return          list of TreeItem(s) representing top classes
     */
    public static ObservableList<TreeItem<Resource>> getRootClasses(OntModel schema) {
        ObservableList<TreeItem<Resource>> items = FXCollections.observableArrayList();

        items.addAll(asList(schema, new SimpleSelector(null, RDFS.subClassOf, OWL.Thing)));
        return items;
    }

    @Override public ObservableList<TreeItem<Resource>> getChildren() {
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
     * - subclasses of an actual Resource if it is a Class
     * - actual members of a class if it is a Class
     * - actual resource properties, except parent (of course)
     *
     * @return  observable list of TreeItem nodes
     */
    private ObservableList<TreeItem<Resource>> buildChildren() {
        ObservableList<TreeItem<Resource>> children = FXCollections.observableArrayList();

        // OWL class hierarchy
        children.addAll(asList(model, new SimpleSelector(null, RDFS.subClassOf, resource)));

        // actual members of a class
        children.addAll(asList(model, new SimpleSelector(null, RDF.type, resource)));

        // properties of a current resource
        StmtIterator iterP = resource.listProperties();
        while (iterP.hasNext()) {
            Statement st = iterP.nextStatement();

            Property predicate = st.getPredicate();
            RDFNode obj = st.getObject();

            // exclude Literals, Parent and Class definitions
            if (obj.isResource() && !obj.equals(getParent().getValue()) &&
                    !obj.equals(OWL.Thing) && !predicate.equals(RDF.type)) {

                children.add(new RDFTreeItem(model, obj.asResource()));
            }
        }

        return children;
    }

    /**
     * A shortcut method to return List of TreeItem(s) from querying the Model.
     *
     * @param m    model to query and to use for new items
     * @param s    selector to query given model
     */
    private static List<TreeItem<Resource>> asList(Model m, SimpleSelector s) {
        List<TreeItem<Resource>> lst = new ArrayList<>();

        StmtIterator iter = m.listStatements(s);
        while (iter.hasNext()) {
            Statement st = iter.nextStatement();
            lst.add(new RDFTreeItem(m, st.getSubject()));
        }

        return lst;
    }
}
