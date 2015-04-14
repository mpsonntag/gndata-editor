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
import javafx.scene.web.WebView;
import javafx.util.Callback;

import gndata.app.html.PageCtrl;
import gndata.app.state.MetadataNavState;
import gndata.app.ui.util.*;
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

    private MetadataNavState metadataState;
    private ObservableList<DataPropertyTableItem> statements;

    @Inject
    public MetadataDetailsCtrl(MetadataNavState metadataState) {
        this.metadataState = metadataState;
        this.statements = FXCollections.observableList(new ArrayList<DataPropertyTableItem>());

        metadataState.selectedNodeProperty().addListener((obs, odlVal, newVal) -> {
            getPage().applyModel(newVal);

            if (newVal == null) {
                statements.clear();
            } else {
                statements.setAll(
                        Resources.streamLiteralsFor(newVal.getResource())
                                .map(DataPropertyTableItem::new)
                                .collect(Collectors.toList())
                );
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
            new Callback<TableColumn, TableCell>() {
                public TableCell call(TableColumn p) {
                    return new EditingCell();
                }
            };

        TableColumn prop = new TableColumn("Property");
        prop.setCellValueFactory(new PropertyValueFactory<DataPropertyTableItem, String>("predicate"));
        TableColumn val = new TableColumn("Value");
        val.setCellValueFactory(new PropertyValueFactory<DataPropertyTableItem, String>("literal"));
        val.setCellFactory(cellFactory);
        val.setOnEditCommit(
                new EventHandler<CellEditEvent<DataPropertyTableItem, String>>() {
                    @Override
                    public void handle(CellEditEvent<DataPropertyTableItem, String> event) {
                        ((DataPropertyTableItem) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())).setLiteral(event.getOldValue(), event.getNewValue());
                    }
                }
        );

        TableColumn type = new TableColumn("Type");
        type.setCellValueFactory(new PropertyValueFactory<DataPropertyTableItem, String>("type"));
        TableColumn edit = new TableColumn("Delete");
        //edit.setCellFactory(cellDelFactory);

        //edit.setCellValueFactory(new PropertyValueFactory<DataPropertyTableItem, String>("icon"));

        tableView.setItems(statements);
        tableView.getColumns().addAll(prop, val, type, edit);
    }

    class EditingCell extends TableCell<DataPropertyTableItem, String> {

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
