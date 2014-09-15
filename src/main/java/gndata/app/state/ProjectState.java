package gndata.app.state;

import gndata.lib.config.ProjectConfig;
import gndata.lib.srv.ProjectService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.inject.Singleton;


/**
 * This class represents a shared state which holds information about the currently
 * loaded project.
 */
@Singleton
public class ProjectState {

    private ObjectProperty<ProjectConfig> config;
    private ObjectProperty<ProjectService> service;

    public ProjectState() {
        config = new SimpleObjectProperty<>();
        service = new SimpleObjectProperty<>();
    }

    public boolean isConfigured() {
        return this.config.get() != null;
    }

    public ProjectConfig getConfig() {
        return config.get();
    }

    public ObjectProperty<ProjectConfig> configProperty() {
        return config;
    }

    public void setConfig(ProjectConfig config) {
        this.config.set(config);
        this.service.set(null);
    }

    public ProjectService getService() {
        return service.get();
    }

    public void setService(ProjectService service) {
        this.service.set(service);
    }
}
