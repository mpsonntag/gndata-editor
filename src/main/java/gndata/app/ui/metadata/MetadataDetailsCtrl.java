// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.metadata;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.*;
import javafx.scene.web.WebView;
import javafx.util.*;

import com.hp.hpl.jena.datatypes.*;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.*;
import gndata.app.html.PageCtrl;
import gndata.app.state.MetadataNavState;
import gndata.app.ui.util.*;
import gndata.lib.util.Resources;

/**
 * Controller for viewing and editing DataProperties.
 */
public class MetadataDetailsCtrl extends PageCtrl {

    // TODO move as much of this to the fxml as possible

    @FXML
    private TogglePane togglePane;
    @FXML
    private WebView webView;
    @FXML
    private TableView<StatementTableItem> tableView;
    @FXML
    private ComboBox<Property> addProperty;
    @FXML
    private TextField addPropertyValue;
    @FXML
    private Label messageLabel;

    private MetadataNavState metadataState;
    private ObservableList<StatementTableItem> statementList;
    private ChangeStatementListListener listListener;
    private ObservableList<Property> availableProperties;

    // check correct data type when a property is updated
    private boolean validUpdateDataType;

    @Inject
    public MetadataDetailsCtrl(MetadataNavState metadataState) {

        this.metadataState = metadataState;

        this.statementList = FXCollections.observableList(new ArrayList<>());
        this.listListener = new ChangeStatementListListener();
        this.statementList.addListener(this.listListener);

        this.availableProperties = FXCollections.observableList(new ArrayList<>());
        this.validUpdateDataType = true;

        metadataState.selectedNodeProperty().addListener((obs, odlVal, newVal) -> {
            getPage().applyModel(newVal);

            // disable statement listener when statement list is
            // replaced by new resource
            this.listListener.setDisabled();

            availableProperties.clear();
            if (newVal == null) {
                statementList.clear();
            } else {
                statementList.setAll(
                        Resources.streamLiteralsFor(newVal.getResource())
                                .map(StatementTableItem::new)
                                .collect(Collectors.toList())
                );

                // TODO implement here: get all available predicates for the selected resource
                // from the metadata service layer

                Resources.streamLiteralsFor(newVal.getResource())
                        .forEach(r -> availableProperties.add(r.getPredicate()));
            }

            this.listListener.setEnabled();

            // TODO at some point replace messageLabel with Popup text bubble
            // TODO implement pretty messageLabel
            // TODO implement messageLabel logic properly
            // clear message label text
            this.messageLabel.setText("");

        });
    }

    @Override
    public WebView getWebView() {
        return webView;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        tableView.setEditable(true);

        // add table columns
        TableColumn prop = new TableColumn("Property");
        prop.setCellValueFactory(new PropertyValueFactory<StatementTableItem, String>("predicate"));

        TableColumn val = new TableColumn("Value");
        val.setCellValueFactory(new PropertyValueFactory<StatementTableItem, String>("literal"));
        val.setCellFactory(cell -> new EditingCell());
        val.setOnEditCommit(new EditCellHandler());

        TableColumn type = new TableColumn("Type");
        type.setCellValueFactory(new PropertyValueFactory<StatementTableItem, String>("type"));

        TableColumn del = new TableColumn("Delete");
        del.setCellFactory(cell -> new DeleteButtonCell());

        tableView.setItems(statementList);
        tableView.getColumns().addAll(prop, val, type, del);

        // TODO ComboBox bug: when the window is maximized, the ComboBox dropdown
        // will extend to outside of the screen; this seems to be a java8u40 bug:
        // http://stackoverflow.com/questions/29127272/javafx-combobox-dropdown-go-out-from-the-edges-of-the-screen

        // manage the addProperty combo box
        // add available properties to combo box; add actual properties, display local name in box
        addProperty.setCellFactory(cell -> new PropertyListCell());

        // manage which text from the property is shown when a combo box item has been selected
        addProperty.setConverter(new PropertyStringConverter());
        addProperty.setItems(availableProperties);
        addProperty.getSelectionModel().selectFirst();

        addPropertyValue.clear();
    }

    // method for creating and adding a new DataProperty to an existing Resource
    public void addDataProperty() {

        if (addProperty.getSelectionModel().getSelectedItem() != null && !addPropertyValue.getText().isEmpty()) {

            // fetch parent resource
            Resource parentResource = metadataState.selectedNodeProperty().getValue().getResource();

            // TODO add value consistency checks e.g. entered value is actually required BigInteger etc.
            // requires model.createTypedLiteral(Object) for now all new entries have to be of type double
            RDFDatatype dt = TypeMapper.getInstance().getTypeByName(XSDDatatype.XSDdouble.getURI());

            if (dt.isValid(addPropertyValue.getText())) {

                System.out.println("Current resource:");
                System.out.println("  URI: "+ parentResource.getURI());
                System.out.println("  NameSpace: "+ parentResource.getNameSpace());
                System.out.println("  LocalName: " + parentResource.getLocalName() + "\n");

                StatementTableItem newSTI = new StatementTableItem(
                        getDataPropertyStatement(parentResource,
                                addProperty.getSelectionModel().getSelectedItem(),
                                addPropertyValue.getText(),
                                dt));

                statementList.add(newSTI);
            } else {

                String warningMsg = "Cannot add property! " + addPropertyValue.getText()
                        + " is not of required type "+ dt.getJavaClass().getSimpleName() +".";

                // display Warning Message PopOver
                CreatePopOver.createPopOver(warningMsg, addProperty, -100);

                messageLabel.setText(warningMsg);
            }
        }
        addPropertyValue.clear();
    }

    // TODO part of the logic of this method should probably be move to the metadata service layer
    // create a new DataProperty for an existing resource
    // return the new DataProperty as Statement
    private Statement getDataPropertyStatement(Resource parentResource, Property selProp, String value, RDFDatatype dt) {
        Property p = ResourceFactory.createProperty(selProp.getNameSpace(), selProp.getLocalName());

        RDFNode o = ResourceFactory.createTypedLiteral(value, dt);

        return ResourceFactory.createStatement(parentResource, p, o);
    }

    // clear message label text when clicked
    public void clearLabel() {
        messageLabel.setText("");
    }


    // -----------------------------------------
    // Custom view classes
    // -----------------------------------------

    private class DeleteButtonCell extends TableCell<StatementTableItem, String> {

        final Button delButton = new Button("x");

        public DeleteButtonCell() {
            delButton.setPadding(new Insets(1));
            delButton.setOnAction(actionEvent -> {
                // retrieve and remove corresponding StatementTableItem
                statementList.remove(tableView.getItems().get(getTableRow().getIndex()));
            });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                setGraphic(delButton);
            } else {
                setGraphic(null);
            }
        }

    }

    // TableCell class required for value updates of an existing DataProperty
    private class EditingCell extends TableCell<StatementTableItem, String> {

        private TextField textField;

        public EditingCell(){}

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

            if(validUpdateDataType) {
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
            } else {
                this.cancelEdit();
                validUpdateDataType = true;
            }
        }

        private void createTextField() {
            textField = new TextField(getContent());
            textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    commitEdit(textField.getText());
                }
            });
        }

        private String getContent() {
            return getItem() == null ? "" : getItem();
        }

    }

    // class handling combo box list cells required to a add a new DataProperty
    private class PropertyListCell extends ListCell<Property> {

        public PropertyListCell() {}

        @Override
        protected void updateItem(Property item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setGraphic(null);
            } else {
                setText(item.getLocalName());
            }
        }
    }

    // class handling which text is displayed when an item from the
    // add new DataProperty combo box has been selected
    private class PropertyStringConverter extends StringConverter<Property> {

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

    // -----------------------------------------
    // Custom Listeners and Event handlers
    // -----------------------------------------

    // Listener class handling any changes to the list of DataProperty Statements
    private class ChangeStatementListListener implements ListChangeListener<StatementTableItem>{

        private boolean enabled = true;

        @Override
        public void onChanged(Change<? extends StatementTableItem> c) {
            if(enabled) {
                while (c.next()) {
                    System.out.println("List has changed: " + c.toString());
                    if (c.wasReplaced()) {
                        System.out.println("  Was replaced: " + c.wasReplaced());
                        System.out.println("  Removed size: " + c.getRemovedSize() + " added size: " + c.getAddedSize());

                        Statement rmstmt = c.getRemoved().get(c.getRemovedSize() - 1).getStatement();
                        Statement addstmt = c.getAddedSubList().get(c.getAddedSize() - 1).getStatement();
                        System.out.println("  Replace " + rmstmt.getSubject() + ": " + rmstmt.getPredicate() + ", " + rmstmt.getLiteral()
                                + "\n with " + addstmt.getSubject() + ": " + addstmt.getPredicate() + ", " + addstmt.getLiteral());

                        if (rmstmt.getLiteral().getDatatype() != null) {
                            System.out.println("  Datatype URI of removed property: "+ rmstmt.getLiteral().getDatatype().getURI());
                            System.out.println("  JC canName removed property: "+
                                    rmstmt.getLiteral().getDatatype().getJavaClass().getCanonicalName());
                        } else {
                            System.out.println("  Plain literal, no DataType available");
                        }

                    } else if (c.wasRemoved()) {
                        System.out.println("  Was removed: " + c.wasRemoved());
                        System.out.println("  Removed size: " + c.getRemovedSize() + " added size: " + c.getAddedSize());

                        Statement rmstmt = c.getRemoved().get(c.getRemovedSize() - 1).getStatement();
                        System.out.println("  Remove "+ rmstmt.getSubject() +": "+ rmstmt.getLiteral());

                    } else if (c.wasAdded()) {
                        System.out.println("  Was added: " + c.wasAdded());
                        System.out.println("  Removed size: " + c.getRemovedSize() + " added size: " + c.getAddedSize());

                        Statement addstmt = c.getAddedSubList().get(c.getAddedSize() - 1).getStatement();
                        System.out.println("  Add "+ addstmt.getSubject() +": "+ addstmt.getPredicate() +", "+ addstmt.getLiteral());
                    }
                    System.out.println("\n");
                }
            }
        }

        public void setEnabled() { this.enabled = true; }

        public void setDisabled() { this.enabled = false; }
    }

    // Event handler class processing StatementTableItem literal value updates
    private class EditCellHandler implements EventHandler<CellEditEvent<StatementTableItem, String>> {

        @Override
        public void handle(CellEditEvent<StatementTableItem, String> event) {

            System.out.println("Edit event type: "+ event.getEventType().toString());

            // here will be a problem, if changes are not immediately persisted:
            // if we mark a Statement for delete, and we try to
            // change the value afterwards, it will probably overwrite the
            // "delete" action with an "update" action.
            // this has to be avoided somehow.

            // TODO implement DataType checks before creating the new statement
            // implement handing over the correct DataType in respect to the value

            String oldVal = event.getOldValue();
            String newVal = event.getNewValue();

            if (!oldVal.equals(newVal)) {
                StatementTableItem oldSTI = (event.getTableView().getItems().get(
                        event.getTablePosition().getRow()));

                // check correct DataType, if available
                StatementTableItem newSTI = null;
                RDFDatatype dt = oldSTI.getStatement().getLiteral().getDatatype();
                if (dt != null) {
                    if (dt.isValid(newVal)) {
                        newSTI = oldSTI.withLiteral(newVal, dt);
                    } else if(newVal.isEmpty()) {
                        messageLabel.setText("Empty values are not allowed.");
                        validUpdateDataType = false;
                    } else {

                        String warningMsg = "Invalid datatype used! " + newVal + " is not of type "
                                + dt.getJavaClass().getSimpleName() + ".";

                        // TODO get the proper cell to hover over
                        CreatePopOver.createPopOver(warningMsg, tableView, 200);

                        messageLabel.setText(warningMsg);
                        validUpdateDataType = false;
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
                    statementList.set(statementList.indexOf(oldSTI), newSTI);
                }
            }
        }
    }

}
