// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.lib.srv;

import gndata.lib.config.ProjectConfig;

/**
 * Class implementing access methods for project related data.
 *
 * TODO implement
 */
public class ProjectService {

    private String basePath;

    public ProjectService(String basePath) {
        this.basePath = basePath;
    }

    public static ProjectService create(ProjectConfig config) {
        return new ProjectService(config.getProjectPath());
    }
}
