package gndata.app.ui.main;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;

import gndata.app.state.*;
import gndata.app.ui.dia.*;
import gndata.lib.config.*;

/**
 * Controller for the main menu view.
 */
public class MenuCtrl implements Initializable {

    private final AppState appState;
    private final ProjectState projectState;

    @FXML
    private MenuBar menu;
    @FXML
    private Menu projectMenu;

    @Inject
    public MenuCtrl(AppState appState, ProjectState projectState) {
        this.appState = appState;
        this.projectState = projectState;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set a listener that hides or shows the project menu depending on the state
        this.projectState.addListener((obs, o, n) -> projectMenu.setVisible(n != null));
    }

    /**
     * Open a new project or create one.
     */
    public void createProject() {
        File selected = showDirectoryChooser();

        if (selected == null)
            return;

        try {
            ProjectConfig config = ProjectConfig.load(selected.getAbsolutePath());
            config = showConfigDialog(config);

            if (config != null) {
                config.store();
                projectState.setConfig(config);

                appState.getConfig().setProject(config.getProjectPath(), config.getName());
                appState.getConfig().store();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows a directory chooser dialog.
     *
     * @return The selected dialog or null if the selection was canceled.
     */
    public File showDirectoryChooser() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select the project directory");
        dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        return dirChooser.showDialog(menu.getScene().getWindow());
    }

    /**
     * Shows a project config dialog.
     *
     * @param config    The configuration to edit in the dialog.
     *
     * @return The edited
     */
    public ProjectConfig showConfigDialog(ProjectConfig config) {
        ProjectConfigView configDialog = new ProjectConfigView(config);
        return configDialog.showDialog(menu.getScene().getWindow());
    }

    /**
     * Show the project settings dialog and save the project settings
     * if they have changed.
     */
    public void projectSettings() {
        try {
            ProjectConfig config = showConfigDialog(projectState.getConfig());

            if (config != null) {
                config.store();
                projectState.setConfig(config);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open a previously opened project.
     */
    public void openProject() {
        String configPath = showListDialog(this.appState.getConfig());

        if (configPath != null) {
            try {
                ProjectConfig config = ProjectConfig.load(configPath);
                projectState.setConfig(config);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String showListDialog(GlobalConfig config) {
        ProjectListView listView = new ProjectListView(config.getProjects());
        return listView.showDialog(menu.getScene().getWindow());
    }

    /**
     * Set the project state to not running.
     */
    public void exit() {
        appState.setRunning(false);
    }

}
