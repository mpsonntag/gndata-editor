// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.main;

import javax.inject.Inject;

import com.google.inject.Injector;
import gndata.app.ui.util.DIView;

/**
 * The main view of the application.
 */
public class MainView extends DIView {

    @Inject
    public MainView(Injector injector) {
        super(injector);
    }

}
