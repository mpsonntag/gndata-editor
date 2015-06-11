// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;

/**
 * Interface for controllers that can be used in a {@link DialogView}.
 */
public abstract class DialogCtrl {

    private boolean cancelled;

    /**
     * Creates a dialog controller with {@link #cancelled} set to true.
     */
    public DialogCtrl() {
        cancelled = true;
    }

    /**
     * Checks whether the dialog was cancelled or not.
     *
     * @return The cancelling state of the dialog.
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether the dialog was cancelled or not.
     *
     * @param cancelled The cancelling state of the dialog.
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Hides the window if it is currently showing.
     */
    protected final void hide(Node v) {
        if (v != null && v.getScene().getWindow().isShowing()) {
            v.getScene().getWindow().hide();
        }
    }

    /**
     * Cancel the editing.
     * Sets {@link #cancelled} to true and hides the window.
     */
    public void cancel(ActionEvent event) {
        setCancelled(true);
        hide((Node) event.getTarget());
    }

    /**
     * Submit the editing result.
     * Sets {@link #cancelled} to false and hides the window.
     */
    public void ok(ActionEvent event) {
        setCancelled(false);
        hide((Node) event.getTarget());
    }
}
