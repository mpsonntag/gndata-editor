package gndata.app.ui.main;

import gndata.app.state.AppState;
import gndata.app.state.ProjectState;
import gndata.app.ui.dia.ProjectConfigView;
import gndata.app.ui.dia.ProjectListView;
import gndata.lib.config.GlobalConfig;
import gndata.lib.config.ProjectConfig;
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

    private final AppState appState;
    private final ProjectState projectState;
    @FXML
    private MenuBar menu;

    @Inject
    public MenuCtrl(AppState appState, ProjectState projectState) {
        this.appState = appState;
        this.projectState = projectState;
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
        System.out.println(configPath);
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
