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
import gndata.app.ui.metadata.manage.*;
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

    private final ObjectProperty<ObservableList<ResourceFileAdapter>> metadataList;
    private final ObjectProperty<MultipleSelectionModel<ResourceFileAdapter>> metadataSelectionModel;
    private final ObjectProperty<SelectionMode> selectionModeProp;

    // Context menu properties
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

        metadataList = new SimpleObjectProperty<>();
        metadataSelectionModel = new SimpleObjectProperty<>();
        selectionModeProp = new SimpleObjectProperty<>();

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

        // Set selection mode of the listView to multiple
        selectionModeProp.setValue(SelectionMode.MULTIPLE);

        navState.selectedNodeProperty().bind(metadataSelectionModel.get().selectedItemProperty());

        metadataList.set(filteredList);

        metadataListView.addEventHandler(MouseEvent.MOUSE_CLICKED, new ListNavigationHandler());

    }


    // -------------------------------------------
    // FXML binding properties
    // -------------------------------------------

    public ObjectProperty<MultipleSelectionModel<ResourceFileAdapter>> metadataSelectionModelProperty() { return metadataSelectionModel; }
    public ObjectProperty<ObservableList<ResourceFileAdapter>> metadataListProperty() { return metadataList; }
    public ObjectProperty<SelectionMode> selectionModeProperty() { return selectionModeProp; }

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

    // Filter the list of resource adapters
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

    // Set context menu content
    public void showContextMenu() {
        final ResourceFileAdapter currRes = metadataSelectionModel.get().getSelectedItem();

        final Statement typeStmt = currRes.getResource().getProperty(RDF.type);
        final String type = typeStmt == null ? "Thing" : typeStmt.getResource().getLocalName();

        final String remLink;
        final String delInst;

        if(metadataSelectionModel.get().getSelectedItems().size() > 1) {
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
        new RenameInstanceCtrl(navState);
    }

    // TODO edit signature once methods from service layer are available
    // Edit selected parent object properties
    public void openManageObjectProperties() {
        System.out.println("Manage parent resource object properties");
    }

    // Add a new instance of the selected resource RDF class
    public void openAddSelectedResource() {
        new AddRDFInstanceCtrl(projectState,
                navState, metadataSelectionModel.get().getSelectedItem().getResource());
        refreshList();
    }

    // Add a new instance of an unspecified RDF class
    public void openAddResource() {
        new AddRDFInstanceCtrl(projectState, navState, null);
        refreshList();
    }

    // Remove objectProperties between parent resource and user selected child resource
    public void removeObjectProperty() {
        ArrayList<Resource> remList = new ArrayList<>();

        metadataSelectionModel.get().getSelectedItems()
                .iterator()
                .forEachRemaining(c -> remList.add(c.getResource()));

        navState.getSelectedParent().removeObjectProperties(remList);

        refreshList();
    }

    // TODO add user validation before actually deleting an instance
    // TODO check navigation, if the to be removed ResourceAdapter is a
    // TODO navParent or navChild. If this is the case, reset the navigation
    // Remove all selected instances from the RDF model
    public void deleteInstance(){

        metadataSelectionModel.get().getSelectedItems()
                .iterator()
                .forEachRemaining(ResourceAdapter::remove);

        refreshList();
    }

    // Refresh the unfiltered resource adapter list and re-apply the filter
    private void refreshList() {

        unfilteredList.clear();
        unfilteredList.addAll(navState.getSelectedParent().getChildren());

        String fltr = filter.get();
        if (fltr == null || fltr.equals("")) {
            applyFilter(null);
        } else {
            applyFilter(fltr);
        }
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

}
