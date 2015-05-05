// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util;

import javafx.scene.control.*;
import javafx.util.Callback;

import com.hp.hpl.jena.datatypes.*;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import gndata.app.ui.util.converter.LiteralConverter;
import gndata.app.ui.util.filter.LiteralFilter;

/**
 * TableCell class required for value updates of an existing DataProperty
 */
public class EditPredicateCellFactory
        implements Callback<TableColumn<StatementTableItem, String>, TableCell<StatementTableItem, String>> {

    private TextField textField;

    @Override
    public TableCell<StatementTableItem, String> call (TableColumn<StatementTableItem, String> p) {
        return new TableCell<StatementTableItem, String>() {

            @Override
            public void startEdit() {
                if(!isEmpty()) {
                    super.startEdit();
                    createTextField();
                    setText(null);
                    setGraphic(textField);
                    textField.selectAll();
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();

                setText(getItem());
                setGraphic(null);
            }

            @Override
            public void updateItem(String item, boolean empty) {

                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        if (textField != null) {
                            textField.setText(getContent());
                        }
                        setText(null);
                        setGraphic(textField);
                    } else {
                        setText(getContent());
                        setGraphic(null);
                    }
                }
            }

            private void createTextField() {

                textField = new TextField(getContent());

                // set textformatter to control textinput dependent on the RDF DataType of the selected Predicate
                RDFDatatype dt = getTableView().getSelectionModel().getSelectedItem().getStatement().getLiteral().getDatatype();
                if (dt == null) { dt = XSDDatatype.XSDstring; }
                textField.setTextFormatter(new TextFormatter(new LiteralConverter(dt), null, new LiteralFilter(dt)));
                textField.setPromptText((dt == null ? "String" : dt.getJavaClass().getSimpleName()) + " value");

                textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        validatedCommit();
                    }
                });
            }

            private String getContent() {
                return getItem() == null ? "" : getItem();
            }

            // TODO this is actually controller logic. Check whether and how this should be moved somewhere else
            private void validatedCommit() {

                String oldVal = getContent();
                String newVal = textField.getText();

                if (!oldVal.equals(newVal)) {
                    StatementTableItem oldSTI = this.getTableView().getItems().get(getTableRow().getIndex());

                    // if available, check correct DataType
                    StatementTableItem newSTI = null;
                    RDFDatatype dt = oldSTI.getStatement().getLiteral().getDatatype();
                    if (dt != null) {
                        if (dt.isValid(newVal)) {
                            newSTI = oldSTI.withLiteral(newVal, dt);
                        }
                    } else {
                        newSTI = oldSTI.withLiteral(newVal);
                    }

                    // a problem could arise, if changes are not immediately transferred to the
                    // RDF data model: if an instance may only have one instance of a
                    // specific data property and this data property is added,
                    // the user could possibly add the same data property twice, if
                    // no checks with the ontology are performed at UI level
                    // to prohibit the second addition.

                    if (newSTI != null) {
                        this.getTableView().getItems().set(this.getTableView().getItems().indexOf(oldSTI), newSTI);
                        commitEdit(textField.getText());
                    } else {
                        cancelEdit();
                    }
                }
            }
        };
    }

}
