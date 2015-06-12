// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.dia;

import gndata.app.ui.util.ValueDialogView;
import gndata.lib.config.ProjectConfig;

/**
 * View for project configuration implemented as a dialog.
 */
public class ProjectConfigView extends ValueDialogView<ProjectConfig> {

    /**
     * Creates a new project configuration view.
     *
     * @param config The configuration to edit in the view.
     */
    public ProjectConfigView(ProjectConfig config) {
        super(new ProjectConfigCtrl(config));
    }
}
