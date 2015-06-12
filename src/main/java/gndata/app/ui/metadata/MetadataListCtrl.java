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
import javafx.collections.transformation.*;
import javafx.event.EventHandler;
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

    private final StringProperty filter;
    private final ObservableList<ResourceFileAdapter> unfilteredList;
    private final FilteredList<ResourceFileAdapter> filteredSortedList;
    private final ObjectProperty<MultipleSelectionModel<ResourceFileAdapter>> metadataListSelectionModel;

    private final ObjectProperty<EventHandler<? super MouseEvent>> listNavEventHandler;

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

        navState.selectedParentProperty().addListener(new SelectedParentListener());
        navState.searchStringProperty().addListener(new SearchStringHandler());

        listNavEventHandler = new SimpleObjectProperty<>(new ListNavigationHandler());

        unfilteredList = FXCollections.observableArrayList();
        filteredSortedList = new FilteredList<>(
                            new SortedList<>(
                                    unfilteredList,
                                    (o,t) -> o.getFileName().compareTo(t.getFileName())
                            )
                        );

        filter = new SimpleStringProperty();
        filter.addListener((p, o, n) -> filteredSortedList.setPredicate(fa -> fa.getFileName().contains(n)));

        metadataListSelectionModel = new SimpleObjectProperty<>();

        cmRename = new SimpleStringProperty();
        cmManageObjProp = new SimpleStringProperty();
        cmAddInstance = new SimpleStringProperty();
        cmAddSelectedInstance = new SimpleStringProperty();
        cmRemoveLink = new SimpleStringProperty();
        cmRemoveInstance = new SimpleStringProperty();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        navState.selectedNodeProperty().bind(metadataListSelectionModel.get().selectedItemProperty());

        // Set selection mode of the listView to multiple
        metadataListSelectionModel.get().setSelectionMode(SelectionMode.MULTIPLE);

    }


    // -------------------------------------------
    // FXML binding properties
    // -------------------------------------------

    public FilteredList<ResourceFileAdapter> getFilteredList() { return filteredSortedList; }
    public ObjectProperty<MultipleSelectionModel<ResourceFileAdapter>> metadataListSelectionModelProperty() { return metadataListSelectionModel; }

    public ObjectProperty<EventHandler<? super MouseEvent>> listNavEventHandlerProperty() { return listNavEventHandler; }
    public StringProperty filterTextFieldProperty() { return filter; }

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

    // TODO check, if all classes actually support RDFS label or if owl:name has to be used
    // implement logic to update the labels of the navigation bar after label has been set
    // how should a refresh actually be done:
    // we directly tap into the resource adapter. the resource adapter is also registered
    // in the navigation bar. should we add an observable (resourceContentChange) to the resource adapter, which
    // state changes, when we update the label. if a resource adapter is added to the navigation bar,
    // we also register a listener to this particular observable of the resource adapter
    // if the state of the observable of the resource adapter changes, the navigation bar
    // calls the private updateButtons method to redraw the whole bar.
    /**
     * Used to edit the RDF label text of the selected parent {@link Resource}
     * Opens a modal stage window
     */
    public void renameParent() {

        String parent = navState.getSelectedParent().getFileName();
        String title = "Rename "+ parent;
        StringDialogView renameView = new StringDialogView(title, parent);
        Optional<String> renameValue = renameView.showAndGet();

        if(renameValue.isPresent() && !renameValue.get().isEmpty()) {
            navState.getSelectedParent().updateLabel(renameValue.get());
        }

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
        AddRDFInstanceView addInst = new AddRDFInstanceView(projectState,
                navState, metadataListSelectionModel.get().getSelectedItem().getResource());
        addInst.show();

        unfilteredList.setAll(navState.getSelectedParent().getChildren());
    }

    /**
     * Used to add a new instance to the parent {@link Resource}
     * Opens a modal stage window
     */
    public void openAddResource() {
        AddRDFInstanceView addInst = new AddRDFInstanceView(projectState,
                navState, null);
        addInst.show();

        unfilteredList.setAll(navState.getSelectedParent().getChildren());
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

        unfilteredList.setAll(navState.getSelectedParent().getChildren());
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

        unfilteredList.setAll(navState.getSelectedParent().getChildren());
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

            unfilteredList.setAll(newValue.getChildren());
            filter.set("");
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

                unfilteredList.setAll(
                        ms.query.streamSearchResults(navState.getSearchString())
                                .map(r -> new ResourceFileAdapter(r, null))
                                .collect(Collectors.toList())
                );
                filter.set("");
            }
        }
    }

}
