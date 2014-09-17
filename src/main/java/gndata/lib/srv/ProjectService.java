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
        ProjectService srv = new ProjectService(config.getProjectPath());
        return srv;
    }

}
