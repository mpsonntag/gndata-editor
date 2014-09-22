// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.lib.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Project configuration;
 */
public class ProjectConfig extends AbstractConfig {

    public static final Path IN_PROJECT_PATH = Paths.get(".gnode", "settings.json");

    public static final Path ONTOLOGY_PATH = Paths.get("metadata", "ontologies", "default.owl");
    public static final Path CUSTOM_ONT_PATH = Paths.get("metadata", "ontologies", "custom.owl");
    public static final Path METADATA_PATH = Paths.get("metadata", "annotations", "metadata.rdf");

    private String projectPath;
    private String name;
    private String description;

    // Metadata configuration
    private String defaultOntologyPath;
    private String customOntologyPath;
    private String metadataStoragePath;

    public String getDefaultOntologyPath() {
        return defaultOntologyPath;
    }

    public void setDefaultOntologyPath(String defaultOntologyPath) {
        this.defaultOntologyPath = defaultOntologyPath;
    }

    public String getCustomOntologyPath() {
        return customOntologyPath;
    }

    public void setCustomOntologyPath(String customOntologyPath) {
        this.customOntologyPath = customOntologyPath;
    }

    public String getMetadataStoragePath() {
        return metadataStoragePath;
    }

    public void setMetadataStoragePath(String metadataStoragePath) {
        this.metadataStoragePath = metadataStoragePath;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Loads the project settings from a json file.
     * If the file does not exist, a default configuration is created.
     *
     * @param projectPath   Path to the project config file.
     *
     * @return The loaded configuration.
     *
     * @throws IOException If the loading fails.
     */
    public static ProjectConfig load(String projectPath) throws IOException {
        Path absPath  = Paths.get(projectPath)
                .toAbsolutePath()
                .normalize();
        Path filePath = absPath.resolve(IN_PROJECT_PATH);

        if (Files.exists(filePath)) {
            return AbstractConfig.load(filePath.toString(), ProjectConfig.class);
        } else {
            ProjectConfig config = new ProjectConfig();
            // set defaults here if necessary
            config.setFilePath(filePath.toString());
            config.setProjectPath(absPath.toString());

            // TODO implement metadata file(s) initialization

            config.setDefaultOntologyPath(absPath.resolve(ONTOLOGY_PATH).toString());
            config.setCustomOntologyPath(absPath.resolve(CUSTOM_ONT_PATH).toString());
            config.setMetadataStoragePath(absPath.resolve(METADATA_PATH).toString());
            config.store();
            return config;
        }
    }

}
