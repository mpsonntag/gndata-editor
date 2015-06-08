// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.metadata.manage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.fxml.*;

import com.hp.hpl.jena.rdf.model.Resource;
import gndata.app.ui.util.*;

/**
 * Controller for the RenameInstanceView
 */
public class RenameInstance2Ctrl extends ValueDialogController<String> implements Initializable {

    private final String initVal;

    private final StringProperty renameLabel;
    private final StringProperty renameValue;
    private final StringProperty promptValue;

    public RenameInstance2Ctrl(String initVal) {

        renameLabel = new SimpleStringProperty();
        renameValue = new SimpleStringProperty();
        this.initVal = initVal;

        promptValue = new SimpleStringProperty();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        renameLabel.set("Rename " + initVal + ":");
        renameValue.set(initVal);
        promptValue.set("Please enter a value");
    }

    @Override
    public String getValue() {
        return renameValue.get();
    }

    // -------------------------------------------
    // FXML binding properties
    // -------------------------------------------

    public final StringProperty renameLabelProperty() { return renameLabel; }
    public final StringProperty renameValueProperty() { return renameValue; }
    public final StringProperty promptValueProperty() { return promptValue; }


    // -----------------------------------------
    // Methods
    // -----------------------------------------

    /**
     * Update the RDF label of the selected parent {@link Resource}, if a value has been provided
     */
    @Override
    public void ok(ActionEvent event) {

        if(renameValue.getValue() != null && !renameValue.getValue().isEmpty()) {
            super.ok(event);
        } else {
            promptValue.set("Please enter a value");
        }
    }

}
