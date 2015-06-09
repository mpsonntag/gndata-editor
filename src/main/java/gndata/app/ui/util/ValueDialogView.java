// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util;

import java.util.Optional;

public abstract class ValueDialogView<T> extends DialogView<T> {

    private final ValueDialogController<T> controller;

    /**
     * Constructor.
     *
     * @param controller The controller which should be passed to the view.
     */
    public ValueDialogView(ValueDialogController<T> controller) {
        super(controller);
        this.controller = controller;
    }

    /**
     * Shows the view as a modal dialog.
     *
     * @return The result of the dialog or Optional.empty() if the dialog was cancelled.
     */
    public Optional<T> showDialog() {

        showWindow();

        if (! controller.isCancelled())
            return Optional.of(controller.getValue());
        else
            return Optional.empty();
    }

}
