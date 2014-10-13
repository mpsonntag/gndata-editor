package gndata.app.ui.util;

import javafx.scene.Node;

/**
 * Interface for controllers that can be used in a {@link DialogView}.
 */
public abstract class DialogController<T> {

    private boolean cancelled;

    /**
     * Creates a dialog controller with {@link #cancelled} set to true.
     */
    public DialogController() {
        cancelled = true;
    }

    /**
     * Return the result of the dialog.
     * If the dialog was cancelled the result should be null.
     *
     * @return The dialog result.
     */
    abstract public T getResult();

    abstract public Node getView();

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
    public void hide() {
        Node v = getView();
        if (v != null && v.getScene().getWindow().isShowing()) {
            v.getScene().getWindow().hide();
        }
    }

    /**
     * Cancel the editing.
     * Sets {@link #cancelled} to true and hides the window.
     */
    public void cancel() {
        setCancelled(true);
        hide();
    }

    /**
     * Submit the editing result.
     * Sets {@link #cancelled} to false and hides the window.
     */
    public void ok() {
        setCancelled(false);
        hide();
    }
}
