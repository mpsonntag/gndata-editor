package gndata.app.ui.util;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

import java.io.IOException;
/**
 * A view class that helps to show a view as a modal dialog.
 * This class does not use dependency injection for controller instantiation. Instead the
 * controller must be provided for the view creation.
 *
 * @param <T> The return type of the dialog.
 */
public abstract class DialogView<T> extends AbstractView {

    private final DialogController<T> controller;

    /**
     * Constructor.
     *
     * @param controller The controller which should be passed to the view.
     */
    public DialogView(DialogController<T> controller) {
        super();
        this.controller = controller;
        getLoader().setControllerFactory(new PseudoFactory(controller));
    }

    /**
     * Shows the view as a modal dialog.
     *
     * @param window The parent window of the dialog.
     *
     * @return The result of the dialog or null if the dialog was cancelled.
     */
    public T showDialog(Window window) {
        try {
            Stage stage = new Stage();
            stage.setScene(new Scene(getScene()));
            stage.setTitle("");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(window);
            stage.showAndWait();
            stage.close();
        } catch (IOException e) {
            // TODO nice exception dialog here
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        if (!controller.isCancelled())
            return controller.getResult();
        else
            return null;
    }

    /**
     * This is necessary because a lambda would lead to a security violation.
     * TODO find a nice way to avoid this class
     */
    private static class PseudoFactory implements Callback<Class<?>, Object> {

        final Object object;

        PseudoFactory(Object object) {
            this.object = object;
        }

        @Override
        public Object call(Class<?> param) {
            return object;
        }
    }

}