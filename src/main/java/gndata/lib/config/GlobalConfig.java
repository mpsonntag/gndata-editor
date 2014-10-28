// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.lib.config;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * Global application configuration.
 */
public class GlobalConfig extends AbstractConfig {

    private Map<String, String> projects;


    public GlobalConfig() {
        projects = new TreeMap<>();
    }


    public Map<String, String> getProjects() {
        return projects;
    }

    public void setProject(String path, String name) {
        path = Paths.get(path)
                .toAbsolutePath()
                .normalize()
                .toString();
        projects.put(path, name);
    }

    public boolean hasProject(String path) {
        path = Paths.get(path)
                .toAbsolutePath()
                .normalize()
                .toString();
        return projects.containsKey(path);
    }

    public String getProjectName(String path) {
        path = Paths.get(path)
                .toAbsolutePath()
                .normalize()
                .toString();
        return projects.get(path);
    }

    /**
     * Loads the project settings from a json file.
     * If the file does not exist, a default configuration is created.
     *
     * @param filePath      Path to the application config file.
     *
     * @return The loaded configuration.
     *
     * @throws java.io.IOException If the loading fails.
     */
    public static GlobalConfig load(String filePath) throws IOException {
        Path tmpPath = Paths.get(filePath)
                .toAbsolutePath()
                .normalize();

        if (Files.exists(tmpPath)) {
            return AbstractConfig.load(tmpPath.toString(), GlobalConfig.class);
        } else {
            GlobalConfig config = new GlobalConfig();
            // set defaults here if necessary
            config.setFilePath(tmpPath.toString());
            config.store();
            return config;
        }
    }

    /**
     * Returns a path to the default configuration file location.
     *
     * TODO Use user\AppData\Roaming\gndata\config.json on windows.
     *
     * @return The path to the configuration file.
     */
    public static String makeConfigPath() {
        return Paths.get(System.getProperty("user.home"), ".gndata", "config.json")
                .toAbsolutePath()
                .toString();
    }

}
