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

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.*;
import javafx.scene.layout.BorderPane;

import gndata.app.ui.util.ValueDialogController;
import gndata.lib.config.ProjectConfig;

/**
 * Controller for the project configuration view.
 */
public class ProjectConfigCtrl extends ValueDialogController<ProjectConfig> implements Initializable {

    @FXML private BorderPane view;

    private final ProjectConfig config;
    private final StringProperty name;
    private final StringProperty description;

    /**
     * Constructor.
     *
     * @param config The current project configuration.
     */
    public ProjectConfigCtrl(ProjectConfig config) {
        this.config = new ProjectConfig(config);
        name = new SimpleStringProperty();
        description = new SimpleStringProperty();
    }

    /**
     * Initializes the controller. This method will only work if
     * the controller was created by an fxml loader and is correctly bound to
     * a view.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.bindBidirectional(config.nameProperty());
        description.bindBidirectional(config.descriptionProperty());
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

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}
