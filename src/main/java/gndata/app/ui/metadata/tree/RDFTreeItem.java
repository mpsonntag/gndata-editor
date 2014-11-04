// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.metadata.tree;

import java.util.*;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;
import gndata.app.ui.metadata.VisualItem;

/**
 * A tree representation of the RDF model graph.
 */
public class RDFTreeItem extends TreeItem<RDFNode> {

    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;
    private boolean isLeaf;

    /**
     * Builds a new TreeItem based on a given RDF Node
     *
     * @param res   an actual RDF Node that a current TreeItem represents
     */
    public RDFTreeItem(RDFNode res) {
        super(res);
    }

    @Override
    public ObservableList<TreeItem<RDFNode>> getChildren() {
        if (isFirstTimeChildren) {
            isFirstTimeChildren = false;
            super.getChildren().setAll(buildChildren());
        }
        return super.getChildren();
    }

    @Override
    public boolean isLeaf() {
        if (isFirstTimeLeaf) {
            isFirstTimeLeaf = false;
            isLeaf = buildChildren().size() == 0;
        }
        return isLeaf;
    }

    private boolean isOntologyRelated(Statement st) {
        Property p = st.getPredicate();
        RDFNode obj = st.getObject();

        return obj.equals(OWL.Thing) || p.equals(RDF.type) || p.equals(RDFS.subClassOf);
    }

    private boolean isEqualToParent(RDFNode node) {
        if (getParent() == null) return false;

        RDFNode pNode = getParent().getValue();
        return pNode.isResource() && node.equals(pNode.asResource());
    }

    /**
     * Builds children items of this TreeItem node. List of children contains subclasses of an actual resource
     * if it is a class, actual members of a class if it is a Class, and actual resource properties (except parent
     * of course).
     *
     * @return All children of the node as a list.
     */
    private List<RDFTreeItem> buildChildren() {
        List<RDFTreeItem> children = new ArrayList<>();

        if (!getValue().isResource()) return children;

        Resource r = getValue().asResource();
        Model m = getValue().getModel();

        // properties and directly related objects of a current resource
        StmtIterator iter = r.listProperties();
        while (iter.hasNext()) {
            Statement st = iter.nextStatement();

            Property p = st.getPredicate();
            RDFNode obj = st.getObject();

            // exclude OWL triples and Literals
            if (!isEqualToParent(obj) && !isOntologyRelated(st) && !obj.isLiteral()) {
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
        } else {

            // reverse relationships
            iter = m.listStatements(null, null, r);
            while (iter.hasNext()) {
                Statement st = iter.nextStatement();

                Resource subj = st.getSubject();

                if (!isEqualToParent(subj) && !isOntologyRelated(st)) {
                    children.add(new RDFTreeItem(subj));
                }
            }
        }

        // sort alphabetically
        children.sort((a, b) -> VisualItem.renderForSorting(a.getValue()).compareTo(
                                VisualItem.renderForSorting(b.getValue())));

        return children;
    }
}
