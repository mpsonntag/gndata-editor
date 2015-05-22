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
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.vocabulary.RDF;
import gndata.app.state.*;
import gndata.app.ui.util.*;
import gndata.lib.util.*;

/**
 * Provides Modal Popup Window with a dynamic form for adding a new RDF Class Instance
 */
public class AddRDFInstance extends BorderPane implements Initializable {

    private final ProjectState projectState;
    private final MetadataNavState navState;
    private final Stage st = new Stage();

    private final ObjectProperty<Insets> currInsets;
    private final StringProperty labelText;

    private final Resource addNewClass;
    private final ObservableList<Resource> newClassesList;
    private final ObjectProperty<ObservableList<Resource>> newClasses;
    private final ObjectProperty<Resource> selectedNewClass;

    private final ObservableList<DataPropertyTableItem> newPredicatesList;
    private final ObjectProperty<ObservableList<DataPropertyTableItem>> newPredicates;

    private final ObservableList<DatatypeProperty> additionalPredicatesList;
    private final ObjectProperty<ObservableList<DatatypeProperty>> additionalPredicates;
    private final ObjectProperty<DatatypeProperty> selectedPredicate;
    private final ObjectProperty<RDFDatatype> addPredType;
    private final StringProperty addPredValue;
    private final StringProperty addPredPromptText;


    public AddRDFInstance(ProjectState projState, MetadataNavState navigationState, Resource extRes){

        projectState = projState;
        navState = navigationState;
        addNewClass = extRes;

        currInsets = new SimpleObjectProperty<>(new Insets(5));
        labelText = new SimpleStringProperty();

        newClassesList = FXCollections.observableArrayList();
        newClasses = new SimpleObjectProperty<>();
        selectedNewClass = new SimpleObjectProperty<>();

        newPredicatesList = FXCollections.observableArrayList();
        newPredicates = new SimpleObjectProperty<>();

        additionalPredicatesList = FXCollections.observableArrayList();
        additionalPredicates = new SimpleObjectProperty<>();
        selectedPredicate = new SimpleObjectProperty<>();
        addPredValue = new SimpleStringProperty();
        addPredType = new SimpleObjectProperty<>();
        addPredPromptText = new SimpleStringProperty();


        // update the contents of the DataProperty table, if a new class has been selected
        selectedNewClass.addListener((observable, oldValue, newValue) -> {

            // reset
            newPredicatesList.clear();
            additionalPredicatesList.clear();

            OntologyHelper oh = projectState.getMetadata().ontmanager;
            oh.listDatatypeProperties(selectedNewClass.get()).stream()
                    .forEach(c -> {
                        //TODO take care of the case of multiple datatypes
                        Set<RDFDatatype> dts = projectState.getMetadata().ontmanager.getRange(c.asDatatypeProperty());
                        RDFDatatype dt = dts.iterator().next();

                        newPredicatesList.add(new DataPropertyTableItem(c.asProperty(), dt));

                        additionalPredicatesList.add(c.asDatatypeProperty());
                    });
            additionalPredicates.set(additionalPredicatesList);
        });

        // listen on the selected new predicate and set corresponding RDF DataType accordingly
        selectedPredicate.addListener((observable, oldValue, newValue) -> {
            addPredPromptText.set("");
            if (observable != null && newValue != null) {
                //TODO take care of the case of multiple datatypes
                Set<RDFDatatype> dts = projectState.getMetadata().ontmanager.getRange(observable.getValue());
                RDFDatatype dt = dts.iterator().next();

                addPredType.set(dt);
                addPredPromptText.set("Enter " + (dt == null ? "String" : dt.getJavaClass().getSimpleName()) + " value");
            }
        });

        // load corresponding FXML and display contents in popup stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddRDFInstance.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
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

        // set application modality to prohibit actions
        // while the add object property window is open.
        st.initModality(Modality.APPLICATION_MODAL);
        st.setTitle("Add new resource");

        final String labelInsert =
                addNewClass == null ? "resource" : addNewClass.getProperty(RDF.type).getResource().getLocalName();

        labelText.set("Add "+ labelInsert +" to: "+ navState.getSelectedParent().getFileName());

        // reset observable class list
        newClassesList.clear();

        // set up available RDF classes available as children for the current parent resource
        // TODO remove the next lines of mockup with the actual list from the metadataservice
        // TODO fetch all available classes for parent RDF class and add them here
        // START mockup
        newClassesList.add(navState.getSelectedParent().getResource());
        if(navState.getSelectedParent().getParent().isPresent()) {
            newClassesList.addAll(navState.getSelectedParent().getParent().get().getResource()); }
        if(addNewClass != null){
            newClassesList.addAll(addNewClass); }
        // END mockup

        // initialize new class combobox list wrapper with
        // observable list of available RDF classes
        newClasses.set(newClassesList);

        // initialize data property tableview list wrapper with
        // observable list of existing DataProperties
        newPredicates.set(newPredicatesList);

        // initialize add data property combobox list wrapper with
        // observable list of additionally addable data properties
        additionalPredicates.set(additionalPredicatesList);
    }


    // -------------------------------------------
    // FXML binding properties
    // -------------------------------------------

    // insets for padding
    public final ObjectProperty<Insets> insetProperty() { return currInsets; }
    // main label text
    public final StringProperty labelTextProperty() { return labelText; }

    // pre-select new class in the UI element, if available
    public final Resource getAddNewClass() { return addNewClass; }
    // list of available classes for adding a new instance
    public final ObjectProperty<ObservableList<Resource>> newClassesProperty() { return newClasses; }
    // retrieve selected class from the UI element
    public final ObjectProperty<Resource> selectedNewClassProperty() { return selectedNewClass; }

    // list of existing data properties for the currently selected new class
    //public final ObjectProperty<ObservableList<StatementTableItem>> newPredicatesProperty() { return newPredicates; }
    public final ObjectProperty<ObservableList<DataPropertyTableItem>> newPredicatesProperty() { return newPredicates; }

    // adding additional data properties for the currently selected new class
    public final ObjectProperty<ObservableList<DatatypeProperty>> additionalPredicatesProperty() { return additionalPredicates; }
    // retrieve selected predicate from the UI element
    public final ObjectProperty<DatatypeProperty> selectedPredicateProperty() { return selectedPredicate; }
    // provide the RDF DataType of a selected predicate to the UI elements
    public final ObjectProperty<RDFDatatype> addPredTypeProperty() { return addPredType; }
    public final StringProperty addPredicateValueProperty() { return addPredValue; }
    public final StringProperty addPredicatePromptTextProperty() { return addPredPromptText; }


    // -----------------------------------------
    // Methods
    // -----------------------------------------
    public void cancel() {
        st.hide();
    }

    // add new class instance and all corresponding data properties to RDF model
    public void addToRDFModel() {
        // get the parent resource where the new Instance should be connected to
        Resource parentResource = navState.getSelectedParent().getResource();

        // TODO check if we actually need an inf model which includes the ontology
        Model newModel = ModelFactory.createDefaultModel();
        Resource newResource = ResourceFactory.createResource(UUID.randomUUID().toString());

        newPredicatesList.iterator().forEachRemaining(
                c -> {
                    if (c.getTextFieldValue() != null && !c.getTextFieldValue().isEmpty()) {
                        RDFNode o = ResourceFactory.createTypedLiteral(c.getTextFieldValue(), c.getDataType());
                        Statement s = ResourceFactory.createStatement(newResource, c.getProperty(), o);
                        newModel.add(s);
                    }
                });

        // TODO find better solution to deal with completely empty property values
        if (newModel.size()>0) {
            // TODO service layer method to get the proper object property
            // TODO to connect the new resource to the parent resource
            Property connectProperty = ResourceFactory.createProperty("GenericNamespace", "GenericLocalName");
            Statement connectStatement = ResourceFactory.createStatement(parentResource, connectProperty, newResource);
            newModel.add(connectStatement);

            // TODO remove once adding to RDF model works
            System.out.println("Model created, contains statement: "+ newModel.size());
            newModel.listStatements().forEachRemaining(c -> System.out.println("\t" + c.getSubject().toString() + " " + c.getPredicate().toString() + " " + c.getObject().toString()));

            // TODO add model to RDF model
            //projectState.getMetadata().add(newModel);

            // TODO add check, if resource was successfully added, only hide if true
            st.hide();
        } else {
            labelText.set("Please add at least one property value. Only properties containing values will be added");
        }
    }

    // Method for creating and adding a new DataProperty to an existing Resource
    public void addDataProperty() {

        if (selectedPredicate.get() != null && ! addPredValue.get().isEmpty()) {

            //TODO take care of the case of multiple datatypes
            Set<RDFDatatype> dts = projectState.getMetadata().ontmanager.getRange(selectedPredicate.get());
            RDFDatatype dt = dts.iterator().next();

            DataPropertyTableItem newDTI = new DataPropertyTableItem(selectedPredicate.get(), dt);
            newDTI.setTextFieldValue(addPredValue.get());
            newPredicatesList.add(newDTI);
        }

        // cleanup after dp has been added
        // TODO reset selected predicate
        //selectedPredicate.set(null);
        addPredValue.set("");
    }

}
