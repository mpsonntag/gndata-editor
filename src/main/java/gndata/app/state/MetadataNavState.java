// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.state;

import javax.inject.Singleton;
import javafx.beans.property.*;
import javafx.collections.*;

import gndata.lib.srv.ResourceFileAdapter;


/**
 * Class that provides state information about metadata selections, changes and
 * other related information.
 */
@Singleton
public class MetadataNavState {

    private final StringProperty searchString;
    private final BooleanProperty showBrowsingResults;
    private final ObjectProperty<ResourceFileAdapter> selectedParent;
    private final ObjectProperty<ResourceFileAdapter> selectedNode;
    private final ObservableList<ResourceFileAdapter> favoriteFolders;
    private final ObservableList<ResourceFileAdapter> navigationPath;



    public MetadataNavState() {
        searchString = new SimpleStringProperty();
        showBrowsingResults = new SimpleBooleanProperty(true);
        selectedParent = new SimpleObjectProperty<>();
        selectedNode = new SimpleObjectProperty<>();

        favoriteFolders = FXCollections.observableArrayList();
        navigationPath = FXCollections.observableArrayList();
    }

    public String getSearchString() {
        return searchString.get();
    }

    public StringProperty searchStringProperty() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString.set(searchString);
    }

    public boolean getShowBrowsingResults() {
        return showBrowsingResults.get();
    }

    public BooleanProperty showBrowsingResultsProperty() {
        return showBrowsingResults;
    }

    public void setShowBrowsingResults(boolean showBrowsingResults) {
        this.showBrowsingResults.set(showBrowsingResults);
    }

    public ResourceFileAdapter getSelectedNode() {
        return selectedNode.get();
    }

    public ObjectProperty<ResourceFileAdapter> selectedNodeProperty() {
        return selectedNode;
    }

    public void setSelectedNode(ResourceFileAdapter selectedNode) {
        this.selectedNode.set(selectedNode);
    }

    public ResourceFileAdapter getSelectedParent() {
        return selectedParent.get();
    }

    public ObjectProperty<ResourceFileAdapter> selectedParentProperty() {
        return selectedParent;
    }

    public void setSelectedParent(ResourceFileAdapter selectedParent) {
        this.selectedParent.set(selectedParent);
    }

    public ObservableList<ResourceFileAdapter> getFavoriteFolders() {
        return favoriteFolders;
    }

    public ObservableList<ResourceFileAdapter> getNavigationPath() {
        return navigationPath;
    }
}
