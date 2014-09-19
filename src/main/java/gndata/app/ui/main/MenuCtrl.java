package gndata.app.ui.main;

import gndata.app.state.AppState;
import gndata.app.state.ProjectState;
import gndata.app.ui.dia.ProjectConfigView;
import gndata.lib.config.ProjectConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.stage.DirectoryChooser;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

/**
 * Controller for the main menu view.
 */
public class MenuCtrl {

    @FXML private MenuBar menu;

    private final AppState appState;
    private final ProjectState projectState;

    @Inject
    public MenuCtrl(AppState appState, ProjectState projectState) {
        this.appState = appState;
        this.projectState = projectState;
    }

    /**
     * Open a new project or create one.
     *
     * @param event The event causing the call of the method.
     */
    public void createProject(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select the project directory");
        dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File selected = dirChooser.showDialog(menu.getScene().getWindow());

        if (selected == null)
            return;

        try {
            ProjectConfig config = ProjectConfig.load(selected.getAbsolutePath());
            ProjectConfigView configDialog = new ProjectConfigView(config);
            config = configDialog.showDialog(menu.getScene().getWindow());

            if (config != null) {
                config.store();
                projectState.setConfig(config);
            }
        } catch (IOException e) {
            // TODO nice exception dialog here
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Open a previously opened project.
     *
     * @param event The event causing the call of the method.
     */
    public void openProject(ActionEvent event) {
        System.out.println("openProject");
    }

    /**
     * Set the project state to not running.
     *
     * @param event The event causing the call of the method.
     */
    public void exit(ActionEvent event) {
        appState.setRunning(false);
    }
}
