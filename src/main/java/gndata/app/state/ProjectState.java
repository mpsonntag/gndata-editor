package gndata.app.state;

import gndata.lib.config.ProjectConfig;
import gndata.lib.srv.MetadataService;
import gndata.lib.srv.ProjectService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

import javax.inject.Singleton;
import java.io.IOException;


/**
 * This class represents a shared state which holds information about the currently
 * loaded project.
 */
@Singleton
public class ProjectState {

    private final ObjectProperty<ProjectConfig> config;

    private ProjectService service;
    private MetadataService metadata;

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

    public synchronized ObjectProperty<ProjectConfig> configProperty() {
        return config;
    }

    public synchronized void setConfig(ProjectConfig config) throws IOException {
        if (config == null) {
            service = null;
            metadata = null;
        } else if (config != this.config.get()) {
            service = ProjectService.create(config);
            metadata = MetadataService.create(config.getProjectPath());
        }
        this.config.set(config);
    }

    public synchronized ProjectService getService() {
        return service;
    }

    public synchronized MetadataService getMetadata() {
        return metadata;
    }
}
