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
import javafx.beans.property.StringProperty;
import javafx.beans.value.*;
import javafx.collections.ObservableList;
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

        //this.notesState.getSelectedFavorites().addListener(new FavoritesNotesListener());

    }

    public void initialize(URL location, ResourceBundle resources) {
        notesList.setCellFactory(cell -> new NotesListCell());

        projectState.configProperty().addListener((p, o, n) -> {
            if (n == null)
                return;

            // reset  when project is loaded
            notesList.getItems().clear();

            // TODO implement retrieving actual notes from project
            notesList.getItems().addAll(getTmpNotes());
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

    private class FavoritesNotesListener implements ChangeListener<NotesFavoritesResourceAdapter> {

        @Override
        public void changed(ObservableValue<? extends NotesFavoritesResourceAdapter> observable, NotesFavoritesResourceAdapter oldValue, NotesFavoritesResourceAdapter newValue) {

            if (newValue == null || oldValue == newValue) {
                return;
            }

            //

        }
    }

    //TODO remove after implementation of real notes
    private static Collection<NotesAdapter> getTmpNotes() {
        Collection<NotesAdapter> tmp = new ArrayList<>();
        LocalDateTime currDateTime;
        String currCont;
        currDateTime = LocalDateTime.of(2014,11,20, 5, 55);
        currCont = "Trial 5 went well, animal was lively crawling around not impeded by surgery.\n";
        currCont += "Trial 6 animal was too weak to participate, data should be excluded from analysis.\n";
        currCont += "Trial 8 animal displayed new behavioral pattern.";
        tmp.add(new NotesAdapter("7", currDateTime, "Harris Kapler", "Session 12", currCont));
        currDateTime = LocalDateTime.of(2014,11,18, 16, 59);
        currCont = "Tested all new stimulus protocols in trials 5-12 respectively, discard protocols 2 and 7.";
        tmp.add(new NotesAdapter("6", currDateTime, "Tina Schroeder", "Stimulus test runs", currCont));
        currDateTime = LocalDateTime.of(2014,11,17, 17, 55);
        currCont = "Used setup 5 today, defective electrode in the first three trials.";
        tmp.add(new NotesAdapter("5", currDateTime, "Tina Schroeder", "Session 8", currCont));
        currDateTime = LocalDateTime.of(2014,11,8, 14, 46);
        currCont = "Stim protocol 7 shows same problem as 2, include into tests of new protocols.";
        tmp.add(new NotesAdapter("4", currDateTime, "Harris Kapler", "", currCont));
        currDateTime = LocalDateTime.of(2014,11,8, 1, 14);
        currCont = "Trial 2 went well, animal behaved as expected.";
        currCont += "Trial 3, best recording so far, mark as reference dataset for presentations.";
        tmp.add(new NotesAdapter("3", currDateTime, "Tina Schroeder", "Further stimulus problem", currCont));
        currDateTime = LocalDateTime.of(2014,11,6, 23, 12);
        currCont = "Found problem with stimulus protocol 2. should implement new protocol";
        tmp.add(new NotesAdapter("2", currDateTime, "Tina Schroeder", "Stimulus problem", currCont));
        currDateTime = LocalDateTime.of(2014,11,6, 13, 0);
        currCont = "Set up training session with animal #122, behaved well, not stressed at all.";
        tmp.add(new NotesAdapter("1", currDateTime, "Harris Kapler", "Training new animal #122", currCont));

        return tmp;
    }
}
