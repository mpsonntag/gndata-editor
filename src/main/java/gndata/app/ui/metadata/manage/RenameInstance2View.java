// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.metadata.manage;

import gndata.app.ui.util.*;

/**
 * View that shows a dialog with a list of recently opened projects.
 */
public class RenameInstance2View extends ValueDialogView<String> {

    /**
     * Constructor.
     *
     * @param initVal The string that is passed to the controller
     */
    public RenameInstance2View(String initVal) {
        super(new RenameInstance2Ctrl(initVal));
    }

}
