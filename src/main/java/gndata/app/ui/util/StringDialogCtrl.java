// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.fxml.*;

/**
 * Controller for the {@link StringDialogView}
 * This controller receives a title and a value string and returns the updated value string.
 */
public class StringDialogCtrl extends ValueDialogCtrl<String> implements Initializable {

    private final String title;
    private final String initVal;

    private final StringProperty titleValue;
    private final StringProperty updateValue;
    private final StringProperty promptValue;

    public StringDialogCtrl(String title, String initVal) {

        this.title = title;
        this.initVal = initVal;

        titleValue = new SimpleStringProperty();
        updateValue = new SimpleStringProperty();
        promptValue = new SimpleStringProperty();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleValue.set(title);
        updateValue.set(initVal);
        promptValue.set("Please enter a value");
    }

    /**
     * Method returning the updated value string to the parent controller.
     */
    @Override
    public String get() {
        return updateValue.get();
    }

    // -------------------------------------------
    // FXML binding properties
    // -------------------------------------------

    public final StringProperty titleValueProperty() { return titleValue; }
    public final StringProperty updateValueProperty() { return updateValue; }
    public final StringProperty promptValueProperty() { return promptValue; }


    // -----------------------------------------
    // Methods
    // -----------------------------------------

    /**
     * The window will only be closed if a proper value has been provided.
     */
    @Override
    public void ok(ActionEvent event) {

        if(updateValue.getValue() != null && !updateValue.getValue().isEmpty()) {
            super.ok(event);
        } else {
            promptValue.set("Please enter a value");
        }
    }

}
