package gndata.app.ui.dia;

import gndata.app.ui.util.DialogView;
import gndata.lib.config.ProjectConfig;

/**
 * View for project configuration implemented as a dialog.
 */
public class ProjectConfigView extends DialogView<ProjectConfig> {

    /**
     * Creates a new project configuration view.
     *
     * @param config The configuration to edit in the view.
     */
    public ProjectConfigView(ProjectConfig config) {
        super(new ProjectConfigCtrl(config));
    }
}
