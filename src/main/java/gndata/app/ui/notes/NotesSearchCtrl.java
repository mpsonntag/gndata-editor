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

/**
 * Controller handling notes search
 */
public class NotesSearchCtrl implements Initializable {
    @FXML
    private TextField searchField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
