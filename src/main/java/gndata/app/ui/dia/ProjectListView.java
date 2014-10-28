// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.dia;

import java.util.Map;

import gndata.app.ui.util.DialogView;

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
