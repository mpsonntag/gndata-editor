// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util.converter;

import javafx.beans.property.*;
import javafx.util.StringConverter;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.rdf.model.*;

/**
 * Converter class handling the allowed text entry of an RDF literal value
 * dependent on a provided XSDDatatype.
 */
public final class LiteralConverter extends StringConverter<Literal> {

    public final ObjectProperty<RDFDatatype> type;

    public LiteralConverter() {
        this((RDFDatatype) null);
    }

    public LiteralConverter(ObjectProperty<RDFDatatype> type) {
        this.type = type;
    }

    public LiteralConverter(RDFDatatype type) {
        this.type = new SimpleObjectProperty<>(type);
    }

    public RDFDatatype getType() {
        return type.get();
    }

    public ObjectProperty<RDFDatatype> typeProperty() {
        return type;
    }

    public void setType(RDFDatatype type) {
        this.type.set(type);
    }

    @Override
    public String toString(Literal object) {
        return object == null ? "" : object.getLexicalForm();
    }

    @Override
    public Literal fromString(String string) {
        Literal l;
        if (type.isNull().get()) {
            l = ResourceFactory.createPlainLiteral(string);
        } else {
            l = ResourceFactory.createTypedLiteral(string, type.get());
        }
        return l;
    }

}
