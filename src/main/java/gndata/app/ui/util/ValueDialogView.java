// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util;

import java.util.Optional;

public abstract class ValueDialogView<T> extends DialogView {

    /**
     * Constructor.
     *
     * @param controller The controller which should be passed to the view.
     */
    public ValueDialogView(ValueDialogCtrl<T> controller) {
        super(controller);
    }

    public T get() {
        //noinspection unchecked
        return ((ValueDialogCtrl<T>) controller).get();
    }

    /**
     * Shows the view as a modal dialog.
     *
     * @return The result of the dialog or Optional.empty() if the dialog was cancelled.
     */
    public Optional<T> showAndGet() {

        show();

        if (! controller.isCancelled())
            return Optional.of(get());
        else
            return Optional.empty();
    }

}
