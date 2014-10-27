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
import gndata.app.ui.util.InjectorView;

/**
 * The view for the main menu bar.
 */
public class MenuView extends InjectorView {

    @Inject
    public MenuView(Injector injector) {
        super(injector);
    }

}
