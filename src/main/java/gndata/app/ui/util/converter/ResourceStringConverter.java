// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util.converter;

import javafx.util.StringConverter;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Class handling the displayed text when a Jena Resource item in a
 * combo box has been selected
 */
final public class ResourceStringConverter extends StringConverter<Resource> {

    public ResourceStringConverter() {}

    @Override
    public String toString(Resource res) {
        return res != null ? res.getLocalName() : null;
    }

    @Override
    public Resource fromString(String str) {
        return null;
    }

}
