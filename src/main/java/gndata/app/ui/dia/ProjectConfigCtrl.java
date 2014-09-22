package gndata.app.ui.dia;

import gndata.app.ui.util.DialogController;
import gndata.lib.config.ProjectConfig;
import javafx.event.ActionEvent;
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

    @FXML private TextField name;
    @FXML private TextArea  description;

    private final ProjectConfig config;

    /**
     * Constructor.
     *
     * @param config The current project configuration.
     */
    public ProjectConfigCtrl(ProjectConfig config) {
        this.config = config;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setText(config.getName());
        description.setText(config.getDescription());
    }

    /**
     * Cancel the editing.
     * Sets {@link #cancelled} to true and hides the window.
     *
     * @param actionEvent The event that triggered the method call.
     */
    public void cancel(ActionEvent actionEvent) {
        setCancelled(true);
        view.getScene().getWindow().hide();
    }

    /**
     * Submit the editing result.
     * Sets {@link #cancelled} to false and hides the window.
     *
     * @param actionEvent The event that triggered the method call.
     */
    public void ok(ActionEvent actionEvent) {
        setCancelled(false);
        view.getScene().getWindow().hide();
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
            config.setName(name.getText());
            config.setDescription(description.getText());
        }

        return config;
    }
}
