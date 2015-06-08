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
import java.util.Optional;

import javafx.scene.Scene;
import javafx.stage.*;

/**
 * A view class that helps to show a view as a modal dialog.
 * This class does not use dependency injection for controller instantiation. Instead the
 * controller must be provided for the view creation.
 *
 * @param <T> The return type of the dialog.
 */
public abstract class SimpleDialogView<T> extends AbstractView {

    private final SimpleDialogController<T> controller;

    /**
     * Constructor.
     *
     * @param controller The controller which should be passed to the view.
     */
    public SimpleDialogView(SimpleDialogController<T> controller) {
        this.controller = controller;
        getLoader().setControllerFactory(cls -> this.controller);

        URL path = NameConventions.styleResource(SimpleDialogView.class);
        getExtraStyles().add(path);
    }

    /**
     * Shows the view as a modal dialog.
     *
     * @return The result of the dialog or null if the dialog was cancelled.
     */
    public Optional<T> showDialog() {
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

        if (! controller.isCancelled())
            return Optional.of(controller.getValue());
        else
            return Optional.empty();
    }

}
