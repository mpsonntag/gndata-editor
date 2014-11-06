// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.dia;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import gndata.app.ui.util.DialogController;
import gndata.lib.config.ProjectConfig;

/**
 * Controller for the project configuration view.
 */
public class ProjectConfigCtrl extends DialogController<ProjectConfig> implements Initializable {

    final ProjectConfig config;

    @FXML
    private BorderPane view;
    @FXML
    private TextField nameInput;
    @FXML
    private TextArea descriptionInput;

    /**
     * Constructor.
     *
     * @param config The current project configuration.
     */
    public ProjectConfigCtrl(ProjectConfig config) {
        this.config = new ProjectConfig(config);
    }

    /**
     * Initializes the controller. This method will only work if
     * the controller was created by an fxml loader and is correctly bound to
     * a view.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameInput.textProperty().bindBidirectional(config.nameProperty());
        descriptionInput.textProperty().bindBidirectional(config.descriptionProperty());
    }

    /**
     * Return the project configuration.
     * If {@link #cancelled} is false the configuration will be updated,
     * otherwise the unmodified configuration is returned.
     *
     * @return The edited project configuration.
     */
    @Override
    public ProjectConfig getValue() {
        return config;
    }

    @Override
    public Node getView() {
        return view;
    }

}
