// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.state;

import java.io.IOException;
import java.nio.file.Paths;
import javax.inject.Singleton;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;

import gndata.lib.config.ProjectConfig;
import gndata.lib.srv.*;


/**
 * This class represents a shared state which holds information about the currently
 * loaded project.
 */
@Singleton
public class ProjectState {

    private final ObjectProperty<ProjectConfig> config;

    private ProjectService service;
    private MetadataService metadata;
    private FileService fileService;

    public ProjectState() {
        config = new SimpleObjectProperty<>();
    }

    public void addListener(ChangeListener<? super ProjectConfig> listener) {
        config.addListener(listener);
    }

    public void removeListener(ChangeListener<? super ProjectConfig> listener) {
        config.removeListener(listener);
    }

    public synchronized boolean isConfigured() {
        return this.config.get() != null;
    }

    public synchronized ProjectConfig getConfig() {
        return config.get();
    }

    public synchronized void setConfig(ProjectConfig config) throws IOException {
        if (config == null) {
            service = null;
            metadata = null;
            fileService = null;
        } else if (config != this.config.get()) {
            service = ProjectService.create(config);
            metadata = MetadataService.create(config.getProjectPath());
            fileService = new FileService(Paths.get(config.getProjectPath()));
        }
        this.config.set(config);
    }

    public synchronized ObjectProperty<ProjectConfig> configProperty() {
        return config;
    }

    public synchronized ProjectService getService() {
        return service;
    }

    public synchronized MetadataService getMetadata() {
        return metadata;
    }

    public synchronized FileService getFileService() { return fileService; }
}
