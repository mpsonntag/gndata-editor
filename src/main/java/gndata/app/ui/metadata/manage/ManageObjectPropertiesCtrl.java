// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.metadata.manage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.*;
import javafx.beans.property.*;
import javafx.beans.property.ObjectProperty;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.*;

import static java.util.Spliterator.DISTINCT;
import static java.util.Spliterator.NONNULL;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.iterator.*;
import com.hp.hpl.jena.vocabulary.*;
import gndata.lib.util.*;
import org.apache.commons.lang3.tuple.Pair;
import gndata.lib.srv.*;
import gndata.app.state.*;


/**
 * Manages the object properties of a selected RDF resource
 */
public class ManageObjectPropertiesCtrl extends Pane implements Initializable {

    private final MetadataNavState navState;
    private final OntologyHelper oh;

    private final ObservableList<Pair<com.hp.hpl.jena.rdf.model.Property, ResourceAdapter>> ownedLinksList;
    private final ObservableList<Pair<com.hp.hpl.jena.rdf.model.Property, ResourceAdapter>> availableLinksList;

    private final ObjectProperty<ObservableList<Pair<com.hp.hpl.jena.rdf.model.Property, ResourceAdapter>>> ownedLinks;
    private final ObjectProperty<ObservableList<Pair<com.hp.hpl.jena.rdf.model.Property, ResourceAdapter>>> availableLinks;

    private final ObjectProperty<MultipleSelectionModel<Pair<com.hp.hpl.jena.rdf.model.Property, ResourceAdapter>>> ownedLinksSelModel;
    private final ObjectProperty<MultipleSelectionModel<Pair<com.hp.hpl.jena.rdf.model.Property, ResourceAdapter>>> availableLinksSelModel;

    private final ObjectProperty<SelectionMode> ownedLinksSelMode;
    private final ObjectProperty<SelectionMode> availableLinksSelMode;

    private final Stage st = new Stage();
    private final ObjectProperty<Insets> paddingInsets;


    public ManageObjectPropertiesCtrl(ProjectState projectState, MetadataNavState navigationState) {

        navState = navigationState;
        oh = projectState.getMetadata().ontmanager;

        ownedLinksList = FXCollections.observableArrayList();
        availableLinksList = FXCollections.observableArrayList();

        ownedLinks = new SimpleObjectProperty<>();
        availableLinks = new SimpleObjectProperty<>();

        ownedLinksSelModel = new SimpleObjectProperty<>();
        availableLinksSelModel = new SimpleObjectProperty<>();

        ownedLinksSelMode = new SimpleObjectProperty<>();
        availableLinksSelMode = new SimpleObjectProperty<>();

        // Padding insets are required here, since the label padding attribute
        // cannot be set in the fxml file w/o eliciting a javafx.fxml.LoadException
        paddingInsets = new SimpleObjectProperty<>(new Insets(5));

        // Load corresponding FXML and display contents in popup stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageObjectProperties.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        st.setScene(new Scene(this));
        st.sizeToScene();
        st.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Set application modality to prohibit actions while the add object property window is open.
        st.initModality(Modality.APPLICATION_MODAL);

        st.setTitle("Manage links of "+ navState.getSelectedParent().getFileName());

        // Set selection mode of both lists to multiple
        ownedLinksSelMode.setValue(SelectionMode.MULTIPLE);
        availableLinksSelMode.setValue(SelectionMode.MULTIPLE);

        setLists();
    }


    // -------------------------------------------
    // FXML binding properties
    // -------------------------------------------

    public final ObjectProperty<Insets> paddingInsetsProperty() { return paddingInsets; }

    public final ObjectProperty<ObservableList
            <Pair<com.hp.hpl.jena.rdf.model.Property, ResourceAdapter>>> ownedLinksItemProperty() { return ownedLinks; }
    public final ObjectProperty<ObservableList
            <Pair<com.hp.hpl.jena.rdf.model.Property, ResourceAdapter>>> availableLinksItemProperty() { return availableLinks; }

    public final ObjectProperty<MultipleSelectionModel
            <Pair<com.hp.hpl.jena.rdf.model.Property, ResourceAdapter>>> ownedLinksSelModelProperty() { return ownedLinksSelModel; }
    public final ObjectProperty<MultipleSelectionModel
            <Pair<com.hp.hpl.jena.rdf.model.Property, ResourceAdapter>>> availableLinksSelModelProperty() { return availableLinksSelModel; }

    public final ObjectProperty<SelectionMode> ownedLinksSelModeProperty() { return ownedLinksSelMode; }
    public final ObjectProperty<SelectionMode> availableLinksSelModeProperty() { return availableLinksSelMode; }


    // -----------------------------------------
    // Methods
    // -----------------------------------------

    // Remove the object properties of all selected items
    // to the parent resource
    public void removeItems(){
        if (ownedLinksSelModel.get() != null && ownedLinksSelModel.get().getSelectedItems() != null) {
            List<Pair<com.hp.hpl.jena.rdf.model.Property,ResourceAdapter>> remList = new ArrayList<>();

            ownedLinksSelModel.get().getSelectedItems().forEach(c -> remList.add(c));

            //TODO replace placeholder method call with proper call to the ResourceAdapter, once its implemented
            //navState.getSelectedParent().removeObjectProperties_(remList);
            removeObjectProperties(navState.getSelectedParent().getResource(), remList);

            clearLists();
            setLists();
        }
    }

    // Add the proper object properties of all selected items
    // to the parent resource
    public void addItems(){

        if (availableLinksSelModel.get() != null && availableLinksSelModel.get().getSelectedItems() != null) {
            List<Pair<com.hp.hpl.jena.rdf.model.Property,ResourceAdapter>> addList = new ArrayList<>();

            availableLinksSelModel.get().getSelectedItems().forEach(c -> addList.add(c));

            //TODO replace placeholder method call with proper call to the ResourceAdapter, once its implemented
            //navState.getSelectedParent().addObjectProperties(addList);
            addObjectProperties(navState.getSelectedParent().getResource(), addList);

            clearLists();
            setLists();
        }
    }

    // TODO: Display Bug in both Listviews:
    // When multiple items are removed from a list and the list is refreshed,
    // it can happen, that the Strings of old items that should no longer be
    // displayed, are still visible, even though they are no proper items
    // and are not selectable as such. Could be a problem with the bindings to
    // the FXML file, that the lists there are not properly cleared, even though
    // the bound lists in the controller have been cleared and refreshed.

    // Clear owned and available object properties lists
    private void clearLists() {
        ownedLinksList.clear();
        availableLinksList.clear();

        ownedLinks.get().clear();
        availableLinks.get().clear();
    }

    // Add the content of both owned and available object properties lists
    private void setLists() {
        //TODO replace placeholder method call with proper call to the ResourceAdapter, once its implemented
        //ownedLinksList.addAll(navState.getSelectedParent().getResources_());
        ownedLinksList.addAll(getResources(navState.getSelectedParent().getResource()));

        //TODO replace placeholder method call with proper call to the ResourceAdapter, once its implemented
/*        oh.listRelated(navState.getSelectedParent().getResource()).stream()
                .forEach(c -> availableLinksList.addAll(
                        navState.getSelectedParent().availableToAdd_(c.getKey(), c.getValue())
                ));*/
        oh.listRelated(navState.getSelectedParent().getResource()).stream()
                .forEach(c -> availableLinksList.addAll(
                        availableToAdd(navState.getSelectedParent().getResource(), c.getKey(), c.getValue())
                ));


        ownedLinks.set(ownedLinksList);
        availableLinks.set(availableLinksList);
    }


    // ------------------------------------------------------------------------------------------------
    // TODO remove the following four methods once they are properly implemented in the ResourceAdapter
    // ------------------------------------------------------------------------------------------------

    // TODO remove me
    private void addObjectProperties(Resource resource, List<Pair<com.hp.hpl.jena.rdf.model.Property,ResourceAdapter>> objs) {
        Model toAdd = ModelFactory.createDefaultModel();

        objs.stream().forEach(c -> toAdd.add(resource, c.getKey(), c.getValue().getResource()));

        resource.getModel().add(toAdd);
    }

    // TODO remove me
    private void removeObjectProperties(Resource resource, List<Pair<com.hp.hpl.jena.rdf.model.Property,ResourceAdapter>> objs) {
        Model toRemove = ModelFactory.createDefaultModel();

        List<Pair<com.hp.hpl.jena.rdf.model.Property, Resource>> tmpList = new ArrayList<>();

        objs.stream().forEach(c -> tmpList.add(Pair.of(c.getKey(), c.getValue().getResource())));

        toRemove.add(resource.listProperties()
                .toList().stream()
                .filter(st -> st.getObject().isResource())
                .filter(st -> tmpList.contains(Pair.of(st.getPredicate(), st.getObject().asResource())))
                .collect(Collectors.toList()));

        toRemove.add(resource.getModel().listStatements(null, null, resource)
                .toList().stream()
                .filter(st -> tmpList.contains(Pair.of(st.getPredicate(), st.getSubject())))
                .collect(Collectors.toList()));

        resource.getModel().remove(toRemove);
    }

    // TODO remove me
    private Set<Pair<com.hp.hpl.jena.rdf.model.Property, ResourceAdapter>> availableToAdd(Resource resource, com.hp.hpl.jena.ontology.ObjectProperty p, OntClass cls) {
        List<Resource> lst = new ArrayList<>();
        Set<Pair<com.hp.hpl.jena.rdf.model.Property, ResourceAdapter>> retList = new HashSet<>();

        // all available Resources of type cls
        List<Resource> available = resource.getModel()
                .listStatements(null, RDF.type, cls).toList().stream()
                .map(Statement::getSubject)
                .filter(res -> !res.equals(resource))  // exclude self
                .collect(Collectors.toList());

        if (p.isFunctionalProperty() && resource.getProperty(p) == null) {
            lst.addAll(available);
        } else {
            lst.addAll(available);

            // already connected Resources
            lst.removeAll(resource.listProperties(p).toList().stream()
                    .map(Statement::getObject)
                    .map(RDFNode::asResource)
                    .collect(Collectors.toList()));
        }

        lst.stream().map(ResourceAdapter::new).forEach(
                c -> retList.add(Pair.of(p, c))
        );

        return retList;
    }

    // TODO remove me
    private Set<Pair<com.hp.hpl.jena.rdf.model.Property, ResourceAdapter>> getResources(Resource resource) {
        int characteristics = DISTINCT | NONNULL;

        ExtendedIterator<Statement> it;

        Set<Pair<com.hp.hpl.jena.rdf.model.Property, ResourceAdapter>> retList = new HashSet<>();

        if (resource.hasProperty(RDF.type, OWL.Class)) {  //resource is an OWL Class

            resource.getModel().listStatements(null, RDF.type, resource).toList().stream()
                    .forEach(c -> retList.add(Pair.of(c.getPredicate(), new ResourceAdapter(c.getSubject()))));

        } else {
            it = resource.listProperties().filterKeep(new ResourceFilter());
            Stream<Statement> forward = StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(it, characteristics), false);

            it = resource.getModel().listStatements(null, null, resource).filterKeep(new ResourceFilter());
            Stream<Statement> reverse = StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(it, characteristics), false);

            Stream.concat(forward, reverse)
                    .sorted(new StatementComparator())
                    .forEach(c -> {
                        Resource tmpRes = c.getSubject().equals(resource) ? c.getObject().asResource() : c.getSubject();
                        retList.add(Pair.of(c.getPredicate(), new ResourceAdapter(tmpRes)));
                    });
        }

        return retList;
    }

    // TODO remove me
    private static class ResourceFilter extends Filter<Statement> {

        @Override
        public boolean accept(Statement stmt) {
            RDFNode o = stmt.getObject();

            return o.isResource() && (! o.isAnon()) && (! stmt.getPredicate().equals(RDF.type));
        }
    }

}
