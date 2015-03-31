// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.filebrowser;

import javax.inject.Inject;

import com.google.inject.Injector;
import gndata.app.ui.util.InjectorView;

/**
 * Controller for {@link FileBrowserView}
 */
public class FileBrowserView extends InjectorView{

    @Inject
    public FileBrowserView(Injector injector) {
        super(injector);
    }
}
