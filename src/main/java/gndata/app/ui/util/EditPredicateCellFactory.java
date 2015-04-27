package gndata.app.ui.util;

import javafx.scene.control.*;
import javafx.util.Callback;

import com.hp.hpl.jena.datatypes.RDFDatatype;

/**
 * TableCell class required for value updates of an existing DataProperty
 */
public class EditPredicateCellFactory
        implements Callback<TableColumn<StatementTableItem, String>, TableCell<StatementTableItem, String>> {

    private TextField textField;

    @Override
    public TableCell<StatementTableItem, String> call (TableColumn<StatementTableItem, String> p) {
        TableCell<StatementTableItem, String> cell = new TableCell<StatementTableItem, String>() {

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
                textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        doValidCommit();
                    }
                });
            }

            private String getContent() {
                return getItem() == null ? "" : getItem();
            }

            private void doValidCommit() {

                // here will be a problem, if changes are not immediately persisted:
                // if we mark a Statement for delete, and we try to
                // change the value afterwards, it will probably overwrite the
                // "delete" action with an "update" action.
                // this has to be avoided somehow.

                // TODO implement DataType checks before creating the new statement
                // implement handing over the correct DataType in respect to the value

                String oldVal = getContent();
                String newVal = textField.getText();
                String message = "";

                if (!oldVal.equals(newVal)) {
                    StatementTableItem oldSTI = this.getTableView().getItems().get(getTableRow().getIndex());

                    // if available, check correct DataType
                    StatementTableItem newSTI = null;
                    RDFDatatype dt = oldSTI.getStatement().getLiteral().getDatatype();
                    if (dt != null) {
                        if (dt.isValid(newVal)) {
                            newSTI = oldSTI.withLiteral(newVal, dt);
                        } else if(newVal.isEmpty()) {
                            message = "Empty values are not allowed.";
                        } else {

                            message = "Invalid datatype used! " + newVal + " is not of type "
                                    + dt.getJavaClass().getSimpleName() + ".";
                        }
                    } else {
                        newSTI = oldSTI.withLiteral(newVal);
                    }

                    // a problem could arise, if we do not immediately add changes to the
                    // data model: if an instance may only have one instance of a
                    // specific data property and this data property is added,
                    // the user could possibly add the same data property twice, if
                    // no checks with the ontology in the UI are performed
                    // to prohibit the second addition.

                    if (newSTI != null) {
                        this.getTableView().getItems().set(this.getTableView().getItems().indexOf(oldSTI), newSTI);
                        commitEdit(textField.getText());
                    } else {
                        // TODO PopOver contains the message but does not display it properly
                        CreatePopOver.createPopOver(message, textField.getParent());
                        cancelEdit();
                    }
                }
            }

        };
        return cell;
    }
}
