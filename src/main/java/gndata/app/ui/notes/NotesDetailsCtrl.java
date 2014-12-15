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
import java.util.ResourceBundle;
import javafx.beans.property.StringProperty;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import com.google.inject.Inject;
import gndata.app.state.NotesState;
import gndata.lib.srv.NotesAdapter;

/**
 * Controller handling notes list
 */
public class NotesDetailsCtrl implements Initializable {
    @FXML
    private ListView<NotesAdapter> notesList;

    private NotesState notesState;


    @Inject
    public NotesDetailsCtrl(NotesState notesState) {
        this.notesState = notesState;
    }

    public void initialize(URL location, ResourceBundle resources) {
        notesList.setCellFactory(cell -> new NotesListCell());

        // TODO implement actual notes
        LocalDateTime calOne = LocalDateTime.of(2014,11,20, 0, 0);
        String contOne = "Trial 5 went well, animal was lively crawling around not impeded by surgery.\n";
        contOne += "Trial 6 animal was too weak to participate, data should be excluded from analysis.\n";
        contOne += "Trial 8 animal displayed new behavioral pattern.";
        notesList.getItems().add(new NotesAdapter("1", calOne, "Harris Kapler", "Session 12", contOne));
        LocalDateTime calTwo = LocalDateTime.of(2014,11,18, 0, 0);
        String contTwo = "Tested all new stimulus protocols in trials 5-12 respectively, discard protocols 2 and 7.";
        notesList.getItems().add(new NotesAdapter("2", calTwo, "Tina Schroeder", "Stimulus test runs", contTwo));
        LocalDateTime calThree = LocalDateTime.of(2014,11,17, 0, 0);
        String contThree = "Used setup 5 today, defective electrode in the first three trials.";
        notesList.getItems().add(new NotesAdapter("2", calThree, "Tina Schroeder", "Session 8", contThree));
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
                dateLine.setValue(item.getDateAuthor());
                title.setValue(item.getTitle());
                content.set(item.getContent());
            }
        }
    }
}
