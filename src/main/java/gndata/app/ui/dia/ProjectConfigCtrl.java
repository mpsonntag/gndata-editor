package gndata.app.ui.dia;

import gndata.app.ui.util.DialogController;
import gndata.lib.config.ProjectConfig;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the project configuration view.
 */
public class ProjectConfigCtrl extends DialogController<ProjectConfig> implements Initializable {

    @FXML private BorderPane view;

    @FXML private TextField nameInput;
    @FXML private TextArea  descriptionInput;

    final ProjectConfig config;
    final SimpleStringProperty name;
    final SimpleStringProperty description;

    /**
     * Constructor.
     *
     * @param config The current project configuration.
     */
    public ProjectConfigCtrl(ProjectConfig config) {
        this.config = config;
        this.name   = new SimpleStringProperty(config.getName());
        this.description = new SimpleStringProperty(config.getDescription());
    }

    /**
     * Initializes the controller. This method will only work if
     * the controller was created by an fxml loader and is correctly bound to
     * a view.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameInput.textProperty().bindBidirectional(name);
        descriptionInput.textProperty().bindBidirectional(description);
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

    /**
     * Hides the window if it is currently showing.
     */
    public void hide() {
        if (view != null && view.getScene().getWindow().isShowing()) {
            view.getScene().getWindow().hide();
        }
    }

    /**
     * Return the project configuration.
     * If {@link #cancelled} is false the configuration will be updated,
     * otherwise the unmodified configuration is returned.
     *
     * @return The edited project configuration.
     */
    @Override
    public ProjectConfig getResult() {
        if (! isCancelled()) {
            config.setName(name.get());
            config.setDescription(description.get());
        }

        return config;
    }

}
