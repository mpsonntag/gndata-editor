// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.state;

import javax.inject.Singleton;
import javafx.collections.*;

import gndata.lib.srv.NotesFavoritesResourceAdapter;

/**
 * Class providing the state information about notes favorites
 * and notes filtering
 */
@Singleton
public class NotesState {

    private final ObservableList<NotesFavoritesResourceAdapter> favorites;
    private final ObservableList<NotesFavoritesResourceAdapter> selectedFavorites;

    public NotesState() {
        favorites = FXCollections.observableArrayList();
        selectedFavorites = FXCollections.observableArrayList();
    }

    public ObservableList<NotesFavoritesResourceAdapter> getFavorites() { return favorites; }

    public ObservableList<NotesFavoritesResourceAdapter> getSelectedFavorites() { return selectedFavorites; }

}
