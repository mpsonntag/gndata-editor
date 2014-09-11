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

/**
 * Project configuration;
 */
public class ProjectConfig extends AbstractConfig {

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
     * @param fileName      The name of the config file.
     *
     * @return The loaded configuration.
     *
     * @throws IOException If the loading fails.
     */
    public static ProjectConfig load(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            return AbstractConfig.load(fileName, ProjectConfig.class);
        } else {
            ProjectConfig config = new ProjectConfig();
            // set defaults here if necessary
            config.store(fileName);
            return config;
        }
    }
}
