// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util;

import javafx.scene.control.*;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.rdf.model.*;
import gndata.app.ui.util.converter.LiteralConverter;
import gndata.app.ui.util.filter.LiteralFilter;

/**
 * Class that implements rendering of table items with RDF literals.
 */
public final class DataPropertyTableItem {

    private final Property dataProperty;
    private final RDFDatatype dataType;
    private final TextField editValue = new TextField();

    public DataPropertyTableItem(Property property, RDFDatatype dt) {
        dataProperty = property;
        dataType = dt;
        editValue.setTextFormatter(new TextFormatter(new LiteralConverter(dt), null, new LiteralFilter(dt)));
        editValue.setPromptText(dt.getJavaClass().getSimpleName() +" value");
    }

    public Property getProperty() {
        return dataProperty;
    }

    public RDFDatatype getDataType() {
        return dataType;
    }

    public void setTextFieldValue(String value) {
        editValue.setText(value);
    }

    public String getTextFieldValue() {
        return editValue.getText();
    }

    // -------------------------------------
    // CellValueFactory getter
    // -------------------------------------
    public String getPredicate() {
        return dataProperty.getLocalName();
    }

    public TextField getTextField() {
        return editValue;
    }

    public String getType() {
        return dataType.getJavaClass().getSimpleName();
    }
}
