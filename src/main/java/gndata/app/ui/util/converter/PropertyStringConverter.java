// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util.converter;

import javafx.util.StringConverter;

import com.hp.hpl.jena.rdf.model.Property;

/**
 * Class handling the displayed text when an item from the
 * add new DataProperty combo box has been selected
 */
final public class PropertyStringConverter extends StringConverter<Property> {

    public PropertyStringConverter() {}

    @Override
    public String toString(Property prop) {
        return prop != null ? prop.getLocalName() : null;
    }

    @Override
    public Property fromString(String str) {
        return null;
    }

}
