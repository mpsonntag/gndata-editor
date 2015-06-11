// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util;

import java.io.IOException;
import java.net.URL;

import javafx.scene.Scene;
import javafx.stage.*;

/**
 * A view class that helps to show a view as a modal dialog.
 * This class does not use dependency injection for controller instantiation. Instead the
 * controller must be provided for the view creation.
 *
 */
public abstract class DialogView extends AbstractView {

    protected final DialogCtrl controller;

    /**
     * Constructor.
     *
     * @param controller The controller which should be passed to the view.
     */
    public DialogView(DialogCtrl controller) {
        this.controller = controller;
        getLoader().setControllerFactory(cls -> this.controller);

        URL path = NameConventions.styleResource(DialogView.class);
        getExtraStyles().add(path);
    }

    /**
     * Creates a new, modal window and displays this window
     * until the stage is hidden.
     */
    public final void show() {
        try {
            Stage stage = new Stage();
            stage.setScene(new Scene(getScene()));
            stage.setTitle("");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            stage.close();
        } catch (IOException e) {
            // TODO nice exception dialog here
            e.printStackTrace();
        }
    }

}
