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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.*;

import com.hp.hpl.jena.rdf.model.Resource;
import gndata.lib.srv.*;
import gndata.lib.util.OntologyHelper;
import gndata.app.state.*;

/**
 * Manages the object properties of a selected RDF resource
 */
public class ManageObjectPropertiesCtrl extends Pane implements Initializable {

    private final ProjectState projState;
    private final MetadataNavState navState;

    private final ObservableList<ResourceAdapter> ownedLinksList;
    private final ObservableList<ResourceAdapter> availableLinksList;

    private final ObjectProperty<ObservableList<ResourceAdapter>> ownedLinks;
    private final ObjectProperty<ObservableList<ResourceAdapter>> availableLinks;

    private final ObjectProperty<MultipleSelectionModel<ResourceAdapter>> ownedLinksSelModel;
    private final ObjectProperty<MultipleSelectionModel<ResourceAdapter>> availableLinksSelModel;

    private final ObjectProperty<SelectionMode> ownedLinksSelMode;
    private final ObjectProperty<SelectionMode> availableLinksSelMode;

    private final Stage st = new Stage();

    private final ObjectProperty<Insets> paddingInsets;

    public ManageObjectPropertiesCtrl(ProjectState projectState, MetadataNavState navigationState) {

        projState = projectState;
        navState = navigationState;

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

        final String parent = navState.getSelectedParent().getFileName();
        st.setTitle("Manage links of " + parent);

        // Set selection mode of both lists to multiple
        ownedLinksSelMode.setValue(SelectionMode.MULTIPLE);
        availableLinksSelMode.setValue(SelectionMode.MULTIPLE);

        ownedLinksList.addAll(navState.getSelectedParent().getResources());

        OntologyHelper oh = projState.getMetadata().ontmanager;
        oh.listRelated(navState.getSelectedParent().getResource()).stream()
                .forEach(c -> availableLinksList.addAll(
                        navState.getSelectedParent().availableToAdd(c.getKey(), c.getValue())
                ));

        ownedLinks.set(ownedLinksList);
        availableLinks.set(availableLinksList);

    }


    // -------------------------------------------
    // FXML binding properties
    // -------------------------------------------

    public final ObjectProperty<Insets> paddingInsetsProperty() { return paddingInsets; }

    public final ObjectProperty<ObservableList<ResourceAdapter>> ownedLinksItemProperty() { return ownedLinks; }
    public final ObjectProperty<ObservableList<ResourceAdapter>> availableLinksItemProperty() { return availableLinks; }

    public final ObjectProperty<MultipleSelectionModel<ResourceAdapter>> ownedLinksSelModelProperty() { return ownedLinksSelModel; }
    public final ObjectProperty<MultipleSelectionModel<ResourceAdapter>> availableLinksSelModelProperty() { return availableLinksSelModel; }

    public final ObjectProperty<SelectionMode> ownedLinksSelModeProperty() { return ownedLinksSelMode; }
    public final ObjectProperty<SelectionMode> availableLinksSelModeProperty() { return availableLinksSelMode; }


    // -----------------------------------------
    // Methods
    // -----------------------------------------

    // Remove the object properties of all selected items
    // to the parent resource
    public void removeItems(){
        List<Resource> remList = new ArrayList<>();
        ownedLinksSelModel.get().getSelectedItems().forEach(
                c -> remList.add(c.getResource()));
        navState.getSelectedParent().removeObjectProperties(remList);

        // reset everything
        ownedLinksList.clear();
        availableLinksList.clear();

        ownedLinks.get().clear();
        availableLinks.get().clear();

        ownedLinksList.addAll(navState.getSelectedParent().getResources());

        OntologyHelper oh = projState.getMetadata().ontmanager;
        oh.listRelated(navState.getSelectedParent().getResource()).stream()
                .forEach(c -> availableLinksList.addAll(
                        navState.getSelectedParent().availableToAdd(c.getKey(), c.getValue())
                ));

        ownedLinks.set(ownedLinksList);
        availableLinks.set(availableLinksList);

    }

    // Add the proper object properties of al selected items
    // to the parent resource
    public void addItems(){
        System.out.println("Add items");
        availableLinksSelModel.get().getSelectedItems().forEach(
                c -> System.out.println("\tCurrRes: "+ c.getResource().getLocalName()));
    }

}