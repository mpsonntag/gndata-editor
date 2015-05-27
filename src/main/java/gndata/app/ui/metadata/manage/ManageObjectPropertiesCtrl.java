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

import gndata.lib.srv.*;
import gndata.lib.util.OntologyHelper;
import gndata.app.state.*;

/**
 * Manages the object properties of a selected RDF resource
 */
public class ManageObjectPropertiesCtrl extends Pane implements Initializable {

    @FXML   private ListView<ResourceAdapter> ownedLinks;
    @FXML   private ListView<ResourceAdapter> availableLinks;

    private final ProjectState projState;
    private final MetadataNavState navState;

    private final ObservableList<ResourceAdapter> ownedList;
    private final ObservableList<ResourceAdapter> availableList;

    private final Stage st = new Stage();

    private final ObjectProperty<Insets> paddingInsets;

    public ManageObjectPropertiesCtrl(ProjectState projectState, MetadataNavState navigationState) {

        projState = projectState;
        navState = navigationState;

        ownedList = FXCollections.observableArrayList();
        availableList = FXCollections.observableArrayList();

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

        ownedLinks.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        availableLinks.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ownedList.addAll(navState.getSelectedParent().getResources());

        OntologyHelper oh = projState.getMetadata().ontmanager;
        oh.listRelated(navState.getSelectedParent().getResource()).stream()
                .forEach(c -> availableList.addAll(
                        navState.getSelectedParent().availableToAdd(c.getKey(), c.getValue())
                ));

        ownedLinks.getItems().addAll(ownedList);
        availableLinks.getItems().addAll(availableList);

    }


    // -------------------------------------------
    // FXML binding properties
    // -------------------------------------------

    public final ObjectProperty<Insets> paddingInsetsProperty() { return paddingInsets; }


    // -----------------------------------------
    // Methods
    // -----------------------------------------

}