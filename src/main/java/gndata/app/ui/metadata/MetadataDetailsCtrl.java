// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.metadata;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.web.WebView;

import com.hp.hpl.jena.datatypes.*;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.rdf.model.Property;
import gndata.app.html.PageCtrl;
import gndata.app.state.MetadataNavState;
import gndata.app.ui.util.*;
import gndata.lib.util.Resources;

/**
 * Controller for viewing and editing DataProperties.
 */
public class MetadataDetailsCtrl extends PageCtrl {

    // TODO move as much of this to fxml as possible

    @FXML
    private TogglePane togglePane;
    @FXML
    private WebView webView;

    private ObjectProperty<XSDDatatype> testDT;

    private StringProperty promptText;

    private MetadataNavState metadataState;

    private final StringProperty message;

    private ObjectProperty<ObservableList<StatementTableItem>> existingPredicates;

    private final StringProperty newDataPropertyValue;

    private ObservableList<StatementTableItem> statementList;
    private ChangeStatementListListener listListener;

    private ObservableList<Property> availablePredicates;
    private ObjectProperty<ObservableList<Property>> availPredList;

    private ObjectProperty<Property> selectedPredicate;


    @Inject
    public MetadataDetailsCtrl(MetadataNavState metadataState) {

        this.metadataState = metadataState;

        testDT = new SimpleObjectProperty<>();

        promptText = new SimpleStringProperty();

        message = new SimpleStringProperty();
        newDataPropertyValue = new SimpleStringProperty();

        existingPredicates = new SimpleObjectProperty<>();

        availPredList = new SimpleObjectProperty<>();
        selectedPredicate = new SimpleObjectProperty<>();

        statementList = FXCollections.observableList(new ArrayList<>());
        listListener = new ChangeStatementListListener();
        statementList.addListener(listListener);

        availablePredicates = FXCollections.observableList(new ArrayList<>());

        metadataState.selectedNodeProperty().addListener((obs, odlVal, newVal) -> {
            getPage().applyModel(newVal);

            // disable statement listener when statement list is replaced
            // after selection of new parent resource
            listListener.setDisabled();

            availablePredicates.clear();
            if (newVal == null) {
                statementList.clear();
            } else {
                statementList.setAll(
                        Resources.streamLiteralsFor(newVal.getResource())
                                .map(StatementTableItem::new)
                                .collect(Collectors.toList())
                );

                // TODO implement here: get all available predicates
                // for the selected resource from the metadata service layer
                Resources.streamLiteralsFor(newVal.getResource())
                        .forEach(r -> availablePredicates.add(r.getPredicate()));
            }

            listListener.setEnabled();

            // TODO at some point replace messageLabel with Popup text bubble
            // TODO implement pretty messageLabel
            // TODO implement messageLabel logic properly
            // clear message label text
            message.set("");

        });
    }

    @Override
    public WebView getWebView() {
        return webView;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        // TODO ComboBox bug: when the window is maximized, the ComboBox dropdown
        // will extend to outside of the screen; this seems to be a java8u40 bug:
        // http://stackoverflow.com/questions/29127272/javafx-combobox-dropdown-go-out-from-the-edges-of-the-screen

        availPredList.set(availablePredicates);

        existingPredicates.set(statementList);

        newDataPropertyValue.set("");

        //TODO: the combobox now requires a property wrapper which includes the property and the corresponding data type...

        XSDDatatype dt = XSDDatatype.XSDdecimal;

        testDT.set(dt);

        promptText.set("Enter "+ (dt == null ? "String" : dt.getJavaClass().getSimpleName()) +" value");

    }

    // -----------------------------------------
    // Properties
    // -----------------------------------------

    public final StringProperty messageProperty() { return message; }

    public final ObjectProperty<ObservableList<StatementTableItem>> existingPredicatesProperty() { return existingPredicates; }

    public final StringProperty newPredicateValueProperty() { return newDataPropertyValue; }

    public final ObjectProperty<ObservableList<Property>> availablePredicatesProperty() { return availPredList; }

    public final ObjectProperty<Property> selectedPredicateProperty() { return selectedPredicate; }

    public final ObjectProperty<XSDDatatype> testDTProperty() { return testDT; }

    public final StringProperty promptTextProperty() { return promptText; }

    // -----------------------------------------
    // Methods
    // -----------------------------------------

    // Method for creating and adding a new DataProperty to an existing Resource
    public void addDataProperty() {

        if (selectedPredicate.get() != null && !newDataPropertyValue.getValue().isEmpty()) {

            // fetch parent resource
            Resource parentResource = metadataState.selectedNodeProperty().getValue().getResource();

            // TODO add value consistency checks e.g. entered value is actually required BigInteger etc.
            // requires model.createTypedLiteral(Object) for now all new entries have to be of type double
            //RDFDatatype dt = TypeMapper.getInstance().getTypeByName(XSDDatatype.XSDdouble.getURI());
            RDFDatatype dt = XSDDatatype.XSDdouble;

            if (dt.isValid(newDataPropertyValue.getValue())) {

                System.out.println("Current resource:");
                System.out.println("  URI: "+ parentResource.getURI());
                System.out.println("  NameSpace: "+ parentResource.getNameSpace());
                System.out.println("  LocalName: " + parentResource.getLocalName() + "\n");

                StatementTableItem newSTI = new StatementTableItem(
                        getDataPropertyStatement(parentResource,
                                selectedPredicate.get(),
                                newDataPropertyValue.getValue(),
                                dt));

                statementList.add(newSTI);
            } else {

                String warningMsg = "Cannot add property! " + newDataPropertyValue.getValue()
                        + " is not of required type "+ dt.getJavaClass().getSimpleName() +".";

                // display Warning Message PopOver
                //CreatePopOver.createPopOver(warningMsg, newPredicate, -100);

                message.set(warningMsg);
            }
        }

        newDataPropertyValue.set("");
    }

    // TODO part of the logic of this method should probably be move to the metadata service layer
    // create a new DataProperty for an existing resource
    // return the new DataProperty as Statement
    private Statement getDataPropertyStatement(Resource parentResource, Property selProp, String value, RDFDatatype dt) {
        Property p = ResourceFactory.createProperty(selProp.getNameSpace(), selProp.getLocalName());

        RDFNode o = ResourceFactory.createTypedLiteral(value, dt);

        return ResourceFactory.createStatement(parentResource, p, o);
    }

    // clear message label text when clicked
    public void clearLabel() {
        message.set("");
    }


    // -----------------------------------------
    // Custom Listeners and Event handlers
    // -----------------------------------------

    // Listener class handling any changes to the list of DataProperty Statements
    private class ChangeStatementListListener implements ListChangeListener<StatementTableItem>{

        private boolean enabled = true;

        @Override
        public void onChanged(Change<? extends StatementTableItem> c) {
            if(enabled) {
                while (c.next()) {
                    System.out.println("List has changed: " + c.toString());
                    if (c.wasReplaced()) {
                        System.out.println("  Was replaced: " + c.wasReplaced());
                        System.out.println("  Removed size: " + c.getRemovedSize() + " added size: " + c.getAddedSize());

                        Statement rmstmt = c.getRemoved().get(c.getRemovedSize() - 1).getStatement();
                        Statement addstmt = c.getAddedSubList().get(c.getAddedSize() - 1).getStatement();
                        System.out.println("  Replace " + rmstmt.getSubject() + ": " + rmstmt.getPredicate() + ", " + rmstmt.getLiteral()
                                + "\n with " + addstmt.getSubject() + ": " + addstmt.getPredicate() + ", " + addstmt.getLiteral());

                        if (rmstmt.getLiteral().getDatatype() != null) {
                            System.out.println("  Datatype URI of removed property: "+ rmstmt.getLiteral().getDatatype().getURI());
                            System.out.println("  JC canName removed property: "+
                                    rmstmt.getLiteral().getDatatype().getJavaClass().getCanonicalName());
                        } else {
                            System.out.println("  Plain literal, no DataType available");
                        }

                    } else if (c.wasRemoved()) {
                        System.out.println("  Was removed: " + c.wasRemoved());
                        System.out.println("  Removed size: " + c.getRemovedSize() + " added size: " + c.getAddedSize());

                        Statement rmstmt = c.getRemoved().get(c.getRemovedSize() - 1).getStatement();
                        System.out.println("  Remove "+ rmstmt.getSubject() +": "+ rmstmt.getLiteral());

                    } else if (c.wasAdded()) {
                        System.out.println("  Was added: " + c.wasAdded());
                        System.out.println("  Removed size: " + c.getRemovedSize() + " added size: " + c.getAddedSize());

                        Statement addstmt = c.getAddedSubList().get(c.getAddedSize() - 1).getStatement();
                        System.out.println("  Add "+ addstmt.getSubject() +": "+ addstmt.getPredicate() +", "+ addstmt.getLiteral());
                    }
                    System.out.println("\n");
                }
            }
        }

        public void setEnabled() { this.enabled = true; }

        public void setDisabled() { this.enabled = false; }
    }

}
