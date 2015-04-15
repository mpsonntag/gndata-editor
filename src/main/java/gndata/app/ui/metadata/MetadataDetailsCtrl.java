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
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import com.hp.hpl.jena.rdf.model.*;
import gndata.app.html.PageCtrl;
import gndata.app.state.MetadataNavState;
import gndata.app.ui.util.*;
import gndata.lib.srv.StatementAdapter;
import gndata.lib.srv.StatementAdapter.Action;
import gndata.lib.util.Resources;

/**
 * Controller for the table to view metadata items.
 */
public class MetadataDetailsCtrl extends PageCtrl {

    @FXML
    private TogglePane togglePane;
    @FXML
    private WebView webView;
    @FXML
    private TableView<DataPropertyTableItem> tableView;
    @FXML
    private ComboBox<String> addProperty;
    @FXML
    private TextField addPropertyValue;

    private MetadataNavState metadataState;
    private ObservableList<DataPropertyTableItem> statements;

    private ObservableList<StatementAdapter> modelChangeList;

    private ObservableList<String> availableProperties;

    @Inject
    public MetadataDetailsCtrl(MetadataNavState metadataState) {
        this.metadataState = metadataState;
        this.statements = FXCollections.observableList(new ArrayList<DataPropertyTableItem>());
        this.modelChangeList = FXCollections.observableList(new ArrayList<StatementAdapter>());
        this.availableProperties = FXCollections.observableList(new ArrayList<String>());

        metadataState.selectedNodeProperty().addListener((obs, odlVal, newVal) -> {
            getPage().applyModel(newVal);

            availableProperties.clear();
            if (newVal == null) {
                statements.clear();
            } else {
                statements.setAll(
                        Resources.streamLiteralsFor(newVal.getResource())
                                .map(DataPropertyTableItem::new)
                                .collect(Collectors.toList())
                );

                //Todo implement here: get all available predicates for the selected resource
                // from the metadata service layer

                System.out.println("Get literals for " + newVal.getResource().getLocalName() + "\n");

                Resources.streamLiteralsFor(newVal.getResource())
                        .forEach(r -> availableProperties.add(r.getPredicate().getLocalName()));
            }
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
        Callback<TableColumn, TableCell> cellFactory =
                p -> new EditingCell();

        // add listener to delete cell, adding the StatementAdapter corresponding
        // to the selected item to the modelchangelist
        // immediately applying the change for the moment
        Callback<TableColumn, TableCell> delCellFactory =
                p -> new DeleteCell();

        // add table columns

        TableColumn prop = new TableColumn("Property");
        prop.setCellValueFactory(new PropertyValueFactory<DataPropertyTableItem, String>("predicate"));

        TableColumn val = new TableColumn("Value");
        val.setCellValueFactory(new PropertyValueFactory<DataPropertyTableItem, String>("literal"));
        val.setCellFactory(cellFactory);
        val.setOnEditCommit(
                new EventHandler<CellEditEvent<DataPropertyTableItem, String>>() {
                    @Override
                    public void handle(CellEditEvent<DataPropertyTableItem, String> event) {

                        // TODO here will be a problem, if changes are not immediately persisted:
                        // if we mark a StatementAdapter for delete, and we try to
                        // change the value afterwards, it will probably overwrite the
                        // "delete" action with an "update" action.
                        // this has to be avoided somehow.

                        String oldVal = event.getOldValue();
                        String newVal = event.getNewValue();

                        (event.getTableView().getItems().get(
                                event.getTablePosition().getRow())).setLiteral(oldVal, newVal);

                        if (!oldVal.equals(newVal)) {
                            // get updated statementadapter
                            StatementAdapter updateItem =
                                    (event.getTableView().getItems().get(
                                            event.getTablePosition().getRow())).getStatementAdapter();
                            // add last change to the changelist
                            modelChangeList.add(updateItem);
                            // apply changes to the model immediately
                            integrateModelChanges();
                        }
                    }
                }
        );

        TableColumn type = new TableColumn("Type");
        type.setCellValueFactory(new PropertyValueFactory<DataPropertyTableItem, String>("type"));

        TableColumn del = new TableColumn("Delete");
        del.setCellValueFactory(new PropertyValueFactory<DataPropertyTableItem, String>("icon"));
        del.setCellFactory(delCellFactory);

        tableView.setItems(statements);
        tableView.getColumns().addAll(prop, val, type, del);

        // add available properties to combo box
        addProperty.setItems(availableProperties);
        System.out.println("blub: " + availableProperties.size() +"\n");
        addProperty.getSelectionModel().selectFirst();

        addPropertyValue.clear();
    }

    // maybe this should be move to the metadata service layer
    public void createNewPredicate() {

        if (addProperty.getSelectionModel().getSelectedItem() != null && !addPropertyValue.getText().isEmpty()) {

            // create Subject Resource
            Resource parentResource = metadataState.selectedNodeProperty().getValue().getResource();

            // create Predicate
            //TODO get correct property namespace
            String propNamespace = "getMyProperNamespace#";
            String propLocalName = addProperty.getSelectionModel().getSelectedItem();
            Property p = ResourceFactory.createProperty(propNamespace, propLocalName);

            // create Literal value
            RDFNode o = ResourceFactory.createPlainLiteral(addPropertyValue.getText());

            Statement s = ResourceFactory.createStatement(parentResource, p, o);

            StatementAdapter sli = new StatementAdapter(s);
            sli.setAction(Action.ADD);

            modelChangeList.add(sli);

            integrateModelChanges();
        }
    }

    // forward all not integrated changes to the model and integrate them there
    private void integrateModelChanges() {
        System.out.println("Integrate model changes:\n");
        // TODO hand over StatementAdapter to the ChangeHelper to integrate Changes
        // into the actual DataModel

        modelChangeList.stream().forEach(
                sli -> System.out.println(
                        "Predicate: "+ sli.getOriginalStatement().getPredicate().toString() +" Action: "+ sli.getAction() +"\n"));

        // clear ChangeList after changes have been integrated into model
        modelChangeList.clear();
    }

    // Cell class required to delete an existing DataProperty
    private class DeleteCell extends TableCell<DataPropertyTableItem, String> {

        public DeleteCell(){
            this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    // get index of clicked cell
                    TableCell c = (TableCell) event.getSource();

                    // check if an actual existing row has been selected
                    if (!c.isEmpty()) {
                        // retrieve corresponding StatementAdapter
                        StatementAdapter currItem = tableView.getItems().get(c.getIndex()).getStatementAdapter();
                        // mark corresponding StatementAdapter for delete
                        currItem.setAction(Action.DELETE);

                        // add item to change list
                        modelChangeList.add(currItem);
                        // apply changes immediately to the data model for now
                        integrateModelChanges();
                    }
                }
            });
        }

    }

    // cell class to update the value of an existing DataProperty
    private class EditingCell extends TableCell<DataPropertyTableItem, String> {

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

            setText((String) getItem());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if(empty){
                setText(null);
                setGraphic(null);
            } else {
                if(isEditing()){
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        commitEdit(textField.getText());
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }

    }

}
