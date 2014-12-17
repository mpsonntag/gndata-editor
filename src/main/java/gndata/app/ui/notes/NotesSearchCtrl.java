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
import javafx.fxml.*;
import javafx.scene.control.TextField;

import com.google.inject.Inject;
import gndata.app.state.*;

/**
 * Controller handling notes search
 */
public class NotesSearchCtrl implements Initializable {
    @FXML
    private TextField searchField;

    private ProjectState projectState;
    private NotesState notesState;

    @Inject
    public NotesSearchCtrl(ProjectState projectState, NotesState notesState) {
        this.projectState = projectState;
        this.notesState = notesState;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        projectState.configProperty().addListener((p, o, n) -> {
            if (n == null)
                return;
            notesState.setResetNotesList(Boolean.FALSE);
        });
    }

    public void resetNotesFilter() {
        if (this.projectState.isConfigured()) {
            notesState.setResetNotesList(Boolean.TRUE);
        }
    }

    public void goBack() {
        // TODO implement
        System.out.println("NotesSearchCtrl: goBack");
    }

    public void goForward() {
        // TODO implement
        System.out.println("NotesSearchCtrl: goForward");
    }
}
