// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.dia;

import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

import gndata.app.ui.util.TwoLineListCell;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import gndata.app.ui.util.ValueDialogCtrl;

/**
 * Controller for the {@link ProjectListView}.
 */
public class ProjectListCtrl extends ValueDialogCtrl<String> implements Initializable {

    private Map<String, String> projects;

    @FXML
    private BorderPane view;
    @FXML
    private ListView<Entry<String, String>> list;

    public ProjectListCtrl(Map<String, String> projects) {
        this.projects = projects;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.setCellFactory(view -> new ProjectListCell());
        list.getItems().addAll(projects.entrySet());
    }

    @Override
    public String get() {
        SelectionModel<Entry<String, String>> sel = list.getSelectionModel();
        if (!sel.isEmpty()) {
            return sel.getSelectedItem().getKey();
        } else {
            return list.getItems().get(0).getKey();
        }
    }

    private class ProjectListCell extends TwoLineListCell<Entry<String, String>> {

        @Override
        protected void update(Entry<String, String> content, boolean empty) {
            if (!empty) {
                lineOne.set(content.getValue());
                lineTwo.set(content.getKey());
            }
        }

    }
}
