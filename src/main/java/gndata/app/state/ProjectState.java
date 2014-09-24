package gndata.app.state;

import gndata.lib.config.ProjectConfig;
import gndata.lib.srv.MetadataService;
import gndata.lib.srv.ProjectService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.inject.Singleton;
import java.io.IOException;


/**
 * This class represents a shared state which holds information about the currently
 * loaded project.
 */
@Singleton
public class ProjectState {

    private final ObjectProperty<ProjectConfig> config;
    private final ObjectProperty<ProjectService> service;
    private final ObjectProperty<MetadataService> metadata;

    public ProjectState() throws IOException {
        config = new SimpleObjectProperty<>();
        service = new SimpleObjectProperty<>();
        metadata = new SimpleObjectProperty<>();

        config.addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                service.set(null);
                metadata.set(null);
            } else if (oldVal != newVal) {
                service.set(ProjectService.create(newVal));
                // TODO add metadata service
                //metadata.set(MetadataService.create(config.get().getProjectPath()));
            }
        });
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

    public synchronized void setConfig(ProjectConfig config) {
        this.config.set(config);
    }

    public synchronized ProjectService getService() {
        return service.get();
    }

    public synchronized MetadataService getMetadata() {
        return metadata.get();
    }
}
