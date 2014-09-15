package gndata.lib.srv;

import gndata.lib.config.ProjectConfig;
import javafx.beans.property.ObjectProperty;

import java.nio.file.Path;

/**
 * Created by adrian on 12.09.14.
 */
public class ProjectService {

    private Path projectPath;
    private ObjectProperty<ProjectConfig> projectConfig;

    public ProjectConfig getProjectConfig()
    {
        return projectConfig.get();
    }

    public ObjectProperty<ProjectConfig> projectConfigProperty()
    {
        return projectConfig;
    }

    public void setProjectConfig(ProjectConfig projectConfig)
    {
        this.projectConfig.set(projectConfig);
    }

    public Path getProjectPath()
    {
        return projectPath;
    }

    public void setProjectPath(Path projectPath)
    {
        this.projectPath = projectPath;
    }

}
