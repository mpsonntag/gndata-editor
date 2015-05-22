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
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import com.google.inject.Inject;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.RDF;
import gndata.app.state.*;
import gndata.app.ui.util.*;
import gndata.lib.srv.*;

/**
 * Controller for the metadata list.
 */
public class MetadataListCtrl implements Initializable {


    @FXML private ListView<ResourceFileAdapter> metadataListView;

    private final ProjectState projectState;
    private final MetadataNavState navState;
    private final StringProperty listFilterText;
    private final StringProperty filter;
    private final ObservableList<ResourceFileAdapter> filteredList;
    private final List<ResourceFileAdapter> unfilteredList;

    // context menu properties
    private final StringProperty cmRename;
    private final StringProperty cmManageObjProp;
    private final StringProperty cmAddInstance;
    private final StringProperty cmAddSelectedInstance;
    private final StringProperty cmRemoveLink;
    private final StringProperty cmRemoveInstance;

    @Inject
    public MetadataListCtrl(ProjectState projState, MetadataNavState navigationState) {
        projectState = projState;
        navState = navigationState;

        filter = new SimpleStringProperty();
        listFilterText = new SimpleStringProperty();
        filteredList = FXCollections.observableArrayList();
        unfilteredList = new ArrayList<>();

        navState.selectedParentProperty().addListener(new SelectedParentListener());
        filter.addListener((p, o, n) -> applyFilter(n));

        navState.searchStringProperty().addListener(new SearchStringHandler());

        cmRename = new SimpleStringProperty();
        cmManageObjProp = new SimpleStringProperty();
        cmAddInstance = new SimpleStringProperty();
        cmAddSelectedInstance = new SimpleStringProperty();
        cmRemoveLink = new SimpleStringProperty();
        cmRemoveInstance = new SimpleStringProperty();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        listFilterText.bindBidirectional(filter);

        // set selection mode of the listView to multiple
        metadataListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        metadataListView.setItems(filteredList);
        navState.selectedNodeProperty().bind(metadataListView.getSelectionModel().selectedItemProperty());

        metadataListView.addEventHandler(MouseEvent.MOUSE_CLICKED, new ListNavigationHandler());
        metadataListView.setCellFactory(ra -> new MetadataListCell());

    }


    // -------------------------------------------
    // FXML binding properties
    // -------------------------------------------

    public StringProperty filterTextFieldProperty() { return listFilterText; }

    // Context menu properties
    public StringProperty cmRenameProperty() { return cmRename; }
    public StringProperty cmManageObjPropProperty() { return cmManageObjProp; }
    public StringProperty cmAddInstanceProperty() { return cmAddInstance; }
    public StringProperty cmAddSelectedInstanceProperty() { return cmAddSelectedInstance; }
    public StringProperty cmRemoveLinkProperty() { return cmRemoveLink; }
    public StringProperty cmRemoveInstanceProperty() { return cmRemoveInstance; }


    // -------------------------------------------
    // Custom methods
    // -------------------------------------------

    // filter the list of resource adapters
    public void applyFilter(String filter) {

        if (filter == null || filter.equals("")) {
            filteredList.setAll(unfilteredList);
        } else {
            filteredList.setAll(
                    unfilteredList.stream()
                            .filter(ra -> ra.getFileName().contains(filter))
                            .collect(Collectors.toList())
            );
        }
    }

    // set context menu content
    public void showContextMenu() {
        final ResourceFileAdapter currRes = metadataListView.getSelectionModel().getSelectedItem();

        final Statement typeStmt = currRes.getResource().getProperty(RDF.type);
        final String type = typeStmt == null ? "Thing" : typeStmt.getResource().getLocalName();

        final String remLink;
        final String delInst;

        if(metadataListView.getSelectionModel().getSelectedItems().size() > 1) {
            remLink = "Remove links to selected resources";
            delInst = "Delete selected resources";
        } else {
            remLink = "Remove link to resource (" + currRes.getFileName() +")";
            delInst = "Delete resource ("+ currRes.getFileName() +")";
        }

        cmRename.set("Rename resource " + navState.getSelectedParent().getFileName());
        cmManageObjProp.set("Manage resource links of " + navState.getSelectedParent().getFileName());
        cmAddInstance.set("Add new resource ...");
        cmAddSelectedInstance.set("Add new " + type);
        cmRemoveLink.set(remLink);
        cmRemoveInstance.set(delInst);
    }

    // Edit the RDF label text of the selected parent resource
    // Open new window for this
    public void renameParent() {
        System.out.println("rename parent resource");
    }

    // TODO edit signature once methods from service layer are available
    // edit selected parent object properties
    public void openManageObjectProperties() {
        System.out.println("Manage parent resource object properties");
    }

    // Add a new instance of the selected resource RDF class
    public void openAddSelectedResource() {
        System.out.println("Add specific resource");
    }

    // Add a new instance of an unspecified RDF class
    public void openAddResource() {
        System.out.println("Add resource");
    }

    // wrapper to check if more than one list items have been selected
    public void removeObjectProperty() {
        System.out.println("RemoveObjectPropertyMain");
    }

    // TODO call service layer and remove link between the objects
    // remove objectProperty between parent resource and child resource
    public void removeObjectProperty(Resource res) {
        System.out.println("RemoveObjectProperty");
    }

    // TODO call service layer and remove link between the objects
    // remove objectProperty between parent resource and multiple child resources
    public void removeObjectProperty(ObservableList<ResourceAdapter> resList) {
        System.out.println("RemoveObjectPropertyList");
    }

    // remove all selected instances from the RDF model
    public void deleteInstance(){
        System.out.println("deleteInstanceMain");
    }

    // TODO add user validation before actually deleting an instance
    // TODO check navigation, if the to be removed ResourceAdapter is a
    // TODO navParent or navChild. If this is the case, reset the navigation
    // delete selected resource from the RDF model and refresh the displayed list.
    public void deleteInstance(ResourceAdapter res) {
        System.out.println("deleteInstance");
    }

    // TODO add user validation before actually deleting an instance
    // TODO check navigation, if any of the to be removed ResourceAdapters is a
    // TODO navParent or navChild. If this is the case, reset the navigation
    // delete all selected resources from the RDF model and refresh the displayed list
    public void deleteInstance(ObservableList<ResourceAdapter> resList) {
        System.out.println("deleteInstances");
    }


    // -------------------------------------------
    // Custom classes
    // -------------------------------------------

    /**
     * Listen for selected parent changes.
     */
    private class SelectedParentListener implements ChangeListener<ResourceFileAdapter> {

        @Override
        public void changed(ObservableValue<? extends ResourceFileAdapter> observable, ResourceFileAdapter oldValue,
                            ResourceFileAdapter newValue) {

            unfilteredList.clear();
            unfilteredList.addAll(newValue.getChildren());

            String fltr = filter.get();
            if (fltr == null || fltr.equals("")) {
                applyFilter(null); // just filter with null
            } else {
                filter.set(null); // reset the filter (triggers filtering)
            }

            navState.setShowBrowsingResults(true);
        }
    }


    /**
     * Listen for double clicks on the list.
     */
    private class ListNavigationHandler extends DoubleClickHandler {

        @Override
        public void handleDoubleClick(MouseEvent mouseEvent) {
            ResourceFileAdapter ra = metadataListView.getSelectionModel().getSelectedItem();
            navState.setSelectedParent(ra);
        }
    }


    /**
     * Listen for changes in the search string property of the navigation state.
     */
    private class SearchStringHandler implements ChangeListener<String> {

        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            MetadataService ms = projectState.getMetadata();

            if (ms != null) {
                unfilteredList.clear();
                unfilteredList.addAll(
                        ms.query.streamSearchResults(navState.getSearchString())
                            .map(r -> new ResourceFileAdapter(r, null))
                            .collect(Collectors.toList())
                );

                String fltr = filter.get();
                if (fltr == null || fltr.equals("")) {
                    applyFilter(null); // just filter with null
                } else {
                    filter.set(null); // reset the filter (triggers filtering)
                }
            }
        }
    }


    /**
     * A list cell for lists showing a {@link ResourceFileAdapter}.
     */
    private class MetadataListCell extends TwoLineListCell<ResourceFileAdapter> {

        @Override
        protected void update(ResourceFileAdapter item, boolean empty) {
            if (! empty) {
                lineOne.set(item.getFileName());
                lineTwo.set(item.toInfoString());
            } else {
                lineOne.set(null);
                lineTwo.set(null);
            }
        }
    }
}
