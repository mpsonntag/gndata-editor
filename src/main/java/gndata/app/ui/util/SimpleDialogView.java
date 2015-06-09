// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util;

/**
 * This view returns a boolean value dependent on
 * the state of the {@link SimpleDialogController}.
 */
public abstract class SimpleDialogView<T> extends DialogView<T> {

    private final SimpleDialogController<T> controller;

    /**
     * Constructor.
     *
     * @param controller The controller which should be passed to the view.
     */
    public SimpleDialogView(SimpleDialogController<T> controller) {
        super(controller);
        this.controller = controller;
    }

    /**
     * Shows the view as a modal dialog.
     *
     * @return The boolean state of the controller or true if the
     * view has been canceled.
     */
    public boolean showDialog() {

        showWindow();

        if (controller.isCancelled())
            return true;
        else
            return controller.isHandlingSuccess();
    }

}
