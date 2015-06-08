// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util;


/**
 * Interface for controllers that can be used in a {@link SimpleDialogView}.
 */
public abstract class SimpleDialogController<T> extends DialogController<T> {

    private boolean handlingSuccess;

    /**
     * Creates a dialog controller with {@link #handlingSuccess} set to true.
     */
    public SimpleDialogController() {
        handlingSuccess = true;
    }

    /**
     * States whether the dialog has been successfully handled or not.
     *
     * @return The successful handling state of the dialog.
     */
    public boolean isHandlingSuccess() {
        return handlingSuccess;
    }

    /**
     * Sets whether the handling of the dialog was successful or not.
     *
     * @param handlingSuccess states whether the dialog has been handled successfully.
     */
    public void setHandlingSuccess(boolean handlingSuccess) {
        this.handlingSuccess = handlingSuccess;
    }

}
