package gndata.app.ui.util;

import com.hp.hpl.jena.rdf.model.*;
import javafx.collections.FXCollections;
import javafx.scene.control.TreeItem;
import javafx.collections.ObservableList;


public class RDFResourceTreeItem extends TreeItem<Resource> {

    private Model model;
    private Resource resource;

    private Property subClassOf;
    private Property isType;

    public RDFResourceTreeItem(Model mod, Resource res) {
        model = mod;
        resource = res;

        String pURI = "http://www.w3.org/2000/01/rdf-schema#subClassOf";
        subClassOf = model.getProperty(pURI);

        String tURI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
        isType = model.getProperty(tURI);
    }

    @Override public ObservableList<TreeItem<Resource>> getChildren() {
        ObservableList<TreeItem<Resource>> children = FXCollections.observableArrayList();

        // OWL class hierarchy
        StmtIterator iterH = model.listStatements(null, subClassOf, resource);
        while (iterH.hasNext()) {
            Statement st = iterH.nextStatement();
            children.add(new RDFResourceTreeItem(model, st.getSubject()));
        }

        // actual members of a class
        StmtIterator iterM = model.listStatements(null, isType, resource);
        while (iterM.hasNext()) {
            Statement st = iterM.nextStatement();
            children.add(new RDFResourceTreeItem(model, st.getSubject()));
        }

        // properties of a current resource
        StmtIterator iterP = resource.listProperties();
        while (iterP.hasNext()) {
            Statement st = iterP.nextStatement();

            Property predicate = st.getPredicate();
            RDFNode obj = st.getObject();
            if (obj.isResource() && !predicate.equals(isType)) {
                children.add(new RDFResourceTreeItem(model, obj.asResource()));
            }
        }

        super.getChildren().setAll(children);

        return super.getChildren();
    }

    @Override public boolean isLeaf() {
        return getChildren().size() == 0;
    }
}
