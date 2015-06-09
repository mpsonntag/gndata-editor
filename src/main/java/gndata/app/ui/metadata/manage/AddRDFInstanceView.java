// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.metadata.manage;

import com.hp.hpl.jena.rdf.model.Resource;
import gndata.app.state.*;
import gndata.app.ui.util.*;

/**
 * Modal dialog view providing a dynamic form for the addition of a new RDF class instance.
  */
public class AddRDFInstanceView extends SimpleDialogView<Boolean> {

    /**
     * Constructor
     * Uses an additional stylesheet
     *
     * @param projState The state of the currently open project
     * @param navigationState The state of the currently used metadata source
     * @param extRes The parent RDF Resource of the new RDF class instance
     */
    public AddRDFInstanceView(ProjectState projState, MetadataNavState navigationState, Resource extRes) {
        super(new AddRDFInstanceCtrl(projState, navigationState, extRes));
    }
}
