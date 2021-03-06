// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util;

import javax.inject.Inject;

import com.google.inject.Injector;

/**
 * Base class for views that use dependency injection (Guice) for controller instantiation.
 */
public class InjectorView extends AbstractView {

    private final Injector injector;

    @Inject
    public InjectorView(Injector injector) {
        this.injector = injector;
        getLoader().setControllerFactory(cls -> this.injector.getInstance(cls));
    }

}
