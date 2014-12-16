// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.notes;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import com.google.inject.Inject;
import gndata.app.state.*;
import gndata.lib.srv.*;

/**
 * Controller handling notes list
 */
public class NotesDetailsCtrl implements Initializable {
    @FXML
    private ListView<NotesAdapter> notesList;

    private ProjectState projectState;
    private NotesState notesState;
    private ObservableList<NotesAdapter> filteredNotes;

    @Inject
    public NotesDetailsCtrl(ProjectState projectState, NotesState notesState) {
        this.projectState = projectState;
        this.notesState = notesState;

        filteredNotes = FXCollections.observableList(new ArrayList<>());

        // add listener to selected notes favorite
        this.notesState.getSelectedFavoritesProperty().addListener(new FavoritesNotesListener());
        // add listener to reset button in search bar
        this.notesState.getResetNotesListProperty().addListener(new ListResetListener());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        notesList.setCellFactory(cell -> new NotesListCell());

        projectState.configProperty().addListener((p, o, n) -> {
            if (n == null)
                return;

            // reset notes list when project is loaded
            notesList.getItems().clear();

            // get unfiltered notes list, when project is loaded
            notesList.getItems().addAll(getUnfilteredNotes());
        });
    }

    private class NotesListCell extends ListCell<NotesAdapter> {

        protected final StringProperty dateLine, title, content;

        public NotesListCell() {
            Label firstLine = new Label();
            dateLine = firstLine.textProperty();

            Label secondLine = new Label();
            title = secondLine.textProperty();
            secondLine.setStyle("-fx-font-weight: bold");

            Label contentBlock = new Label();
            content = contentBlock.textProperty();

            setGraphic(new VBox(firstLine, secondLine, contentBlock, new Separator()));
        }

        @Override
        protected final void updateItem(NotesAdapter item, boolean empty) {
            super.updateItem(item, empty);

            dateLine.setValue("");
            title.setValue("");
            content.setValue("");

            if ( !empty ) {
                String ls = System.getProperty("line.separator");
                dateLine.setValue(item.getDateAuthor());
                title.setValue(String.format("%s%s%s",item.getTitle(), ls, ls));
                content.set(String.format("%s%s%s",item.getContent(), ls, ls));
            }
        }
    }

    //TODO a similar listener is used in NotesFavoritesCtrl, maybe could they be merged
    private class ListResetListener implements ChangeListener<Boolean> {

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (notesState.getResetNotesList()){

                // reset notes and add all unfiltered notes
                notesList.getItems().clear();
                notesList.getItems().addAll(getUnfilteredNotes());
                notesState.setResetNotesList(Boolean.FALSE);
            }
            return;
        }
    }

    private class FavoritesNotesListener implements ChangeListener<NotesFavoritesResourceAdapter> {

        @Override
        public void changed(ObservableValue<? extends NotesFavoritesResourceAdapter> observable, NotesFavoritesResourceAdapter oldValue, NotesFavoritesResourceAdapter newValue) {

            // reset notes list and filtered list
            if (notesList != null) { notesList.getItems().clear(); }
            if (filteredNotes != null) { filteredNotes.clear(); }

            // set notes list to unfiltered notes
            notesList.getItems().addAll(getUnfilteredNotes());

            if (newValue == null || oldValue == newValue) {
                return;
            }

            // use selected favorite note value or rdfType to filter notes
            SimpleStringProperty currFilter = new SimpleStringProperty();
            currFilter.setValue("");
            if (! newValue.getValue().isEmpty()) {
                currFilter.setValue(newValue.getValue());
            } else if (!newValue.getRdfType().isEmpty()) {
                currFilter.setValue(newValue.getRdfType());
            }

            // filter notes list using only lower case terms
            notesList.getItems().stream()
                    .filter(ad -> ad.getInfo().toLowerCase().contains(currFilter.getValue().toLowerCase()))
                    .forEach(ad -> filteredNotes.add(ad));

            // reset notesList and add only filtered list
            notesList.getItems().clear();
            notesList.getItems().addAll(filteredNotes);
        }
    }


    private static Collection<NotesAdapter> getUnfilteredNotes() {
        Collection<NotesAdapter> unfilteredNotes = new ArrayList<>();

        //TODO implementation retrieval of unfiltered notes list from rdf resource
        LocalDateTime currDateTime;
        String currCont;
        currDateTime = LocalDateTime.of(2014,11,20, 5, 55);
        currCont = "Trial 5 went well, animal was lively crawling around not impeded by surgery.\n";
        currCont += "Trial 6 animal was too weak to participate, data should be excluded from analysis.\n";
        currCont += "Trial 8 animal displayed new behavioral pattern.";
        unfilteredNotes.add(new NotesAdapter("7", currDateTime, "Harris Kepler", "Session 12", currCont));
        currDateTime = LocalDateTime.of(2014,11,18, 16, 59);
        currCont = "Tested all new stimulus protocols in trials 5-12 respectively, discard protocols 2 and 7.";
        unfilteredNotes.add(new NotesAdapter("6", currDateTime, "Tina Schroeder", "Stimulus test runs", currCont));
        currDateTime = LocalDateTime.of(2014,11,17, 17, 55);
        currCont = "Used setup 5 today, defective electrode in the first three trials.";
        unfilteredNotes.add(new NotesAdapter("5", currDateTime, "Tina Schroeder", "Session 8", currCont));
        currDateTime = LocalDateTime.of(2014,11,8, 14, 46);
        currCont = "Stim protocol 7 shows same problem as 2, include into tests of new protocols.";
        unfilteredNotes.add(new NotesAdapter("4", currDateTime, "Harris Kepler", "", currCont));
        currDateTime = LocalDateTime.of(2014,11,8, 1, 14);
        currCont = "Trial 2 went well, animal behaved as expected.";
        currCont += "Trial 3, best recording so far, mark as reference dataset for presentations.";
        unfilteredNotes.add(new NotesAdapter("3", currDateTime, "Tina Schroeder", "Further stimulus problem", currCont));
        currDateTime = LocalDateTime.of(2014,11,6, 23, 12);
        currCont = "Found problem with stimulus protocol 2. should implement new protocol";
        unfilteredNotes.add(new NotesAdapter("2", currDateTime, "Tina Schroeder", "Stimulus problem", currCont));
        currDateTime = LocalDateTime.of(2014,11,6, 13, 0);
        currCont = "Set up training session with animal #122, behaved well, not stressed at all.";
        unfilteredNotes.add(new NotesAdapter("1", currDateTime, "Harris Kepler", "Training new animal #122", currCont));

        return unfilteredNotes;
    }
}
