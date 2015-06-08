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
 * Controller for the StringDialogView
 * This controller receives a title and a value string.
 */
public class StringDialogCtrl extends ValueDialogController<String> implements Initializable {

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

    @Override
    public String getValue() {
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
     * Close window only in the case of a proper update value
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
