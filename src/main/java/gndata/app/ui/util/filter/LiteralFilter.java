// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util.filter;

import java.util.function.UnaryOperator;
import javafx.beans.property.*;
import javafx.scene.control.TextFormatter.Change;

import com.hp.hpl.jena.datatypes.RDFDatatype;

/**
 * Filter text input dependent on a provided XSDDatatype
 */
public final class LiteralFilter implements UnaryOperator<Change> {

    public final ObjectProperty<RDFDatatype> type;

    public LiteralFilter() {
        this((RDFDatatype) null);
    }

    public LiteralFilter(ObjectProperty<RDFDatatype> type) { this.type = type; }

    public LiteralFilter(RDFDatatype type) { this.type = new SimpleObjectProperty<>(type); }

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
    public Change apply(Change change) {
        // TODO does not accept "-" as the first letter of a number
        // "-" can be entered, if there are already numbers in the textfield
        if (change.isContentChange() && type.isNotNull().get() && !change.getControlNewText().isEmpty()) {
            // System.out.println(type.get().getURI()+" "+change.getControlNewText());

            if (!type.get().isValid(change.getControlNewText())) {
                return null;
            }
        }
        return change;
    }

}
