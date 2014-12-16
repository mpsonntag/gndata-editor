// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.notes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.StringProperty;
import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.scene.control.*;

import com.google.inject.Inject;
import gndata.app.state.*;
import gndata.lib.srv.NotesFavoritesResourceAdapter;

/**
 * Controller handling notes favorites
 */
public class NotesFavoritesCtrl implements Initializable {

    @FXML
    private ListView<NotesFavoritesResourceAdapter> notesFavorites;
    @FXML
    private Button openNotesFavoritesHandling;

    private ProjectState projectState;
    private NotesState notesState;

    @Inject
    public NotesFavoritesCtrl(ProjectState projectState, NotesState notesState) {
        this.projectState = projectState;
        this.notesState = notesState;

        // add listener to reset button in search bar
        this.notesState.getResetNotesListProperty().addListener(new ListResetListener());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        notesFavorites.setCellFactory(cells -> new NotesFavoritesListCell());

        projectState.configProperty().addListener((p, o, n) -> {
            if (n == null)
                return;

            //reset when a project is loaded
            this.notesState.getFavorites().clear();

            //TODO load actual favorites from project settings
            NotesFavoritesResourceAdapter curr;
            curr = new NotesFavoritesResourceAdapter("92b3dz7f0-c650-135c-d7c2-c352c51314a9", "Author", "", Boolean.FALSE);
            notesState.getFavorites().add(curr);
            curr = new NotesFavoritesResourceAdapter("959b57f0-ad50-435c-a7c2-c252c8131b59", "Author", "Harris Kepler", Boolean.FALSE);
            notesState.getFavorites().add(curr);
            curr = new NotesFavoritesResourceAdapter("720710ea-dbd0-40ff-ae27-2dfa57b580d7", "Author", "Tina Schroeder", Boolean.FALSE);
            notesState.getFavorites().add(curr);
            curr = new NotesFavoritesResourceAdapter("4e23f193-785f-47a2-838f-b0601de2478b", "Animal", "", Boolean.FALSE);
            notesState.getFavorites().add(curr);
            curr = new NotesFavoritesResourceAdapter("b485cc80-e207-441d-967a-a6f14d59d411", "Stimulus protocol", "", Boolean.FALSE);
            notesState.getFavorites().add(curr);
        });

        notesFavorites.setItems(notesState.getFavorites());

        notesFavorites.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> {
            if (n == null)
                return;

            notesState.setSelectedFavorites(n);
        });
    }


    //TODO a similar listener is used in NotesDetailsCtrl, maybe could they be merged
    private class ListResetListener implements ChangeListener<Boolean> {

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (notesState.getResetNotesList()){

                // reset favorites selection to make previously selected favorite responsive again
                notesFavorites.getSelectionModel().clearSelection();
                notesState.setSelectedFavorites(null);
            }
            return;
        }
    }

    private class NotesFavoritesListCell extends ListCell<NotesFavoritesResourceAdapter> {

        protected final StringProperty title;

        public NotesFavoritesListCell() {
            Label titleLabel = new Label();
            title = titleLabel.textProperty();

            setGraphic(titleLabel);
        }

        @Override
        protected final void updateItem(NotesFavoritesResourceAdapter item, boolean empty) {
            super.updateItem(item, empty);

            title.setValue("");

            if ( !empty ) {
                title.setValue(String.format("%s:%s", item.getRdfType(), item.getValue()));
            }
        }
    }
}
