package gndata.app.ui.dia;

import gndata.app.ui.util.DialogView;

import java.util.Map;

/**
 * View that shows a dialog with a list of recently opened projects.
 */
public class ProjectListView extends DialogView<String> {

    /**
     * Constructor.
     *
     * @param projects The controller which should be passed to the view.
     */
    public ProjectListView(Map<String, String> projects) {
        super(new ProjectListCtrl(projects));
    }

}
