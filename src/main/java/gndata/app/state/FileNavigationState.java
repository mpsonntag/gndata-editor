// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.state;

import java.util.ArrayList;
import javax.inject.Singleton;
import javafx.beans.property.*;
import javafx.collections.*;

import gndata.lib.srv.*;

/**
 * Created by msonntag on 03.12.14.
 */
@Singleton
public final class FileNavigationState {

    private final ObjectProperty<LocalFile> selectedParent;
    private final ObjectProperty<LocalFile> selectedFile;
    private final ObservableList<LocalFile> favoriteFolders;
    private final ObservableList<LocalFile> navigationPath;

    public FileNavigationState() {
        selectedParent = new SimpleObjectProperty<>();
        selectedFile = new SimpleObjectProperty<>();

        favoriteFolders = FXCollections.observableList(new ArrayList<>());
        navigationPath = FXCollections.observableList(new ArrayList<>());
    }

    public LocalFile getSelectedParent() {
        return selectedParent.get();
    }

    public ObjectProperty<LocalFile> selectedParentProperty() {
        return selectedParent;
    }

    public void setSelectedParent(LocalFile selectedParent) {
        this.selectedParent.set(selectedParent);
    }

    public LocalFile getSelectedFile() {
        return selectedFile.get();
    }

    public ObjectProperty<LocalFile> selectedFileProperty() {
        return selectedFile;
    }

    public void setSelectedFile(LocalFile selectedFile) {
        this.selectedFile.set(selectedFile);
    }

    public ObservableList<LocalFile> getFavoriteFolders() { return favoriteFolders; }

    public ObservableList<LocalFile> getNavigationPath() {
        return navigationPath;
    }
}
