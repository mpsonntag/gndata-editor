// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.lib.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Project configuration;
 */
public class ProjectConfig extends AbstractConfig {

    public static final Path IN_PROJECT_PATH = Paths.get(".gnode", "settings.json");

    private String name;
    private String description;

    public ProjectConfig() {}

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
     * @param filePath      Path to the project config file.
     *
     * @return The loaded configuration.
     *
     * @throws IOException If the loading fails.
     */
    public static ProjectConfig load(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            return AbstractConfig.load(filePath, ProjectConfig.class);
        } else {
            ProjectConfig config = new ProjectConfig();
            // set defaults here if necessary
            config.store(filePath);
            return config;
        }
    }

    /**
     * Returns a path to the default configuration file location based on the
     * project path.
     *
     * @param projectPath   Path to the project root.
     *
     * @return The path to the configuration file.
     */
    public static String makeConfigPath(String projectPath) {
        return Paths.get(projectPath, IN_PROJECT_PATH.toString()).toString();
    }
}
