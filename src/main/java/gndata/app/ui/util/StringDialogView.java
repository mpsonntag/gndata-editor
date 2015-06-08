// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util;

/**
 * View that shows a dialog with a list of recently opened projects.
 */
public class StringDialogView extends ValueDialogView<String> {

    /**
     * Constructor.
     *
     * @param title String containing the title that the modal window label will display
     * @param initVal String containing the initial value that is passed to the controller
     */
    public StringDialogView(String title, String initVal) {
        super(new StringDialogCtrl(title, initVal));
    }

}
