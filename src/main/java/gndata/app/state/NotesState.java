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

import gndata.lib.srv.NotesFavoritesResourceAdapter;

/**
 * Class providing the state information about notes favorites
 * and notes filtering
 */
@Singleton
public class NotesState {

    private final ObservableList<NotesFavoritesResourceAdapter> favorites;
    private final ObjectProperty<NotesFavoritesResourceAdapter> selectedFavorites;

    public NotesState() {
        favorites = FXCollections.observableArrayList();
        selectedFavorites = new SimpleObjectProperty<>();
    }

    public ObservableList<NotesFavoritesResourceAdapter> getFavorites() { return favorites; }

    public ObjectProperty<NotesFavoritesResourceAdapter> getSelectedFavoritesProperty() { return selectedFavorites; }

    public NotesFavoritesResourceAdapter getSelectedFavorites() { return selectedFavorites.get(); }

    public void setSelectedFavorites(NotesFavoritesResourceAdapter selectedFavorite) {
        this.selectedFavorites.set(selectedFavorite);
    }

}
