package gndata.app.ui.util;

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
}
