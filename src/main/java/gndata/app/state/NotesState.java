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
    private final ObjectProperty<Boolean> resetNotesList;

    public NotesState() {
        favorites = FXCollections.observableArrayList();
        selectedFavorites = new SimpleObjectProperty<>();
        resetNotesList = new SimpleObjectProperty<>();
    }

    public ObservableList<NotesFavoritesResourceAdapter> getFavorites() { return favorites; }

    public ObjectProperty<NotesFavoritesResourceAdapter> getSelectedFavoritesProperty() { return selectedFavorites; }

    public NotesFavoritesResourceAdapter getSelectedFavorites() { return selectedFavorites.get(); }

    public void setSelectedFavorites(NotesFavoritesResourceAdapter selectedFavorite) {
        this.selectedFavorites.set(selectedFavorite);
    }

    public ObjectProperty<Boolean> getResetNotesListProperty() { return resetNotesList; }

    public Boolean getResetNotesList() { return resetNotesList.get(); }

    public void setResetNotesList(Boolean resetNotesList) {
        this.resetNotesList.set(resetNotesList);
    }

}
