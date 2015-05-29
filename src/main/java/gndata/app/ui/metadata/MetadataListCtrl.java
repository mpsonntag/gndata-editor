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
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import com.google.inject.Inject;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.RDF;
import gndata.app.state.*;
import gndata.app.ui.metadata.manage.*;
import gndata.app.ui.util.*;
import gndata.lib.srv.*;

/**
 * Controller for the metadata list. The provided context menu
 * grants access to various options for the management of
 * the RDF data model. These options include
 * Adding or editing the RDF label of the parent {@link Resource}
 * Managing the {@link com.hp.hpl.jena.ontology.ObjectProperty} of the parent {@link Resource}
 * Adding a new {@link Resource} to the parent {@link Resource}
 * Removing {@link com.hp.hpl.jena.ontology.ObjectProperty} to child {@link Resource}
 * Deleting child {@link Resource}s from the RDF {@link Model}
 */
public class MetadataListCtrl implements Initializable {

    private final ProjectState projectState;
    private final MetadataNavState navState;

    private final StringProperty listFilterText;
    private final StringProperty filter;
    private final ObservableList<ResourceFileAdapter> filteredList;
    private final List<ResourceFileAdapter> unfilteredList;

    private final EventHandler<MouseEvent> listNavEventHandler;
    private final ObjectProperty<EventHandler<? super MouseEvent>> listNavEventHandlerProp;

    private final ObjectProperty<ObservableList<ResourceFileAdapter>> metadataList;
    private final ObjectProperty<MultipleSelectionModel<ResourceFileAdapter>> metadataListSelectionModel;
    private final ObjectProperty<SelectionMode> metadataListSelectionMode;

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
        metadataListSelectionModel = new SimpleObjectProperty<>();
        metadataListSelectionMode = new SimpleObjectProperty<>();

        listNavEventHandler = new ListNavigationHandler();
        listNavEventHandlerProp = new SimpleObjectProperty<>();

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

        // Set selection mode of the listView to multiple
        metadataListSelectionMode.setValue(SelectionMode.MULTIPLE);

        listNavEventHandlerProp.set(listNavEventHandler);

        listFilterText.bindBidirectional(filter);
        metadataList.set(filteredList);

        navState.selectedNodeProperty().bind(metadataListSelectionModel.get().selectedItemProperty());

    }


    // -------------------------------------------
    // FXML binding properties
    // -------------------------------------------

    public ObjectProperty<MultipleSelectionModel<ResourceFileAdapter>> metadataListSelectionModelProperty() { return metadataListSelectionModel; }
    public ObjectProperty<ObservableList<ResourceFileAdapter>> metadataListProperty() { return metadataList; }
    public ObjectProperty<SelectionMode> selectionModeProperty() { return metadataListSelectionMode; }

    public ObjectProperty<EventHandler<? super MouseEvent>> listNavEventHandlerPropProperty() { return listNavEventHandlerProp; }

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

    /**
     * Filter the list of {@link ResourceAdapter}s
     * @param filter
     */
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

    /**
     * Set ContextMenu content
     */
    public void showContextMenu() {
        final ResourceFileAdapter currRes = metadataListSelectionModel.get().getSelectedItem();

        final Statement typeStmt = currRes.getResource().getProperty(RDF.type);
        final String type = typeStmt == null ? "Thing" : typeStmt.getResource().getLocalName();

        final String remLink;
        final String delInst;

        if(metadataListSelectionModel.get().getSelectedItems().size() > 1) {
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

    /**
     * Used to edit the RDF label text of the selected parent {@link Resource}
     * Opens a modal stage window
     */
    public void renameParent() {
        new RenameInstanceCtrl(navState);
    }

    /**
     * Used to manage the {@link com.hp.hpl.jena.ontology.ObjectProperty} of the parent {@link Resource}
     * Opens a modal stage window
     */
    public void openManageObjectProperties() {
        System.out.println("Manage parent resource object properties");
    }

    /**
     * Used to add a new instance with the same RDF class as the {@link Resource}
     * currently selected in the ListView to the parent {@link Resource}.
     * Opens a modal stage window
     */
    public void openAddSelectedResource() {
        new AddRDFInstanceCtrl(projectState,
                navState, metadataListSelectionModel.get().getSelectedItem().getResource());
        refreshList();
    }

    /**
     * Used to add a new instance to the parent {@link Resource}
     * Opens a modal stage window
     */
    public void openAddResource() {
        new AddRDFInstanceCtrl(projectState, navState, null);
        refreshList();
    }

    /**
     * Used to remove {@link com.hp.hpl.jena.ontology.ObjectProperty} between parent {@link Resource} and
     * the {@link Resource}s currently selected in the ListView.
     */
    public void removeObjectProperty() {
        ArrayList<Resource> remList = new ArrayList<>();

        metadataListSelectionModel.get().getSelectedItems()
                .iterator()
                .forEachRemaining(c -> remList.add(c.getResource()));

        navState.getSelectedParent().removeObjectProperties(remList);

        refreshList();
    }

    // TODO add user validation before actually deleting an instance
    // TODO check navigation, if the to be removed ResourceAdapter is a
    // TODO navParent or navChild. If this is the case, reset the navigation
    /**
     * Used to delete all selected {@link Resource}s from the RDF {@link Model}
     */
    public void deleteInstance(){

        metadataListSelectionModel.get().getSelectedItems()
                .iterator()
                .forEachRemaining(ResourceAdapter::remove);

        refreshList();
    }

    /**
     * Refresh the unfiltered {@link ResourceAdapter} list and re-apply the filter
     */
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
     * Listen for double clicks on the list.
     */
    private class ListNavigationHandler extends DoubleClickHandler {

        @Override
        public void handleDoubleClick(MouseEvent mouseEvent) {
            ResourceFileAdapter ra = metadataListSelectionModel.get().getSelectedItem();
            navState.setSelectedParent(ra);
        }
    }


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
