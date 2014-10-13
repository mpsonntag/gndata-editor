package gndata.app.state;

import com.google.inject.Singleton;
import gndata.lib.config.GlobalConfig;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

@Singleton
public class AppState {

    private final SimpleObjectProperty<GlobalConfig> config;
    private final SimpleBooleanProperty running;

    public AppState() {
        running = new SimpleBooleanProperty(true);
        config = new SimpleObjectProperty<>(null);
    }

    public boolean isRunning() {
        return running.get();
    }

    public SimpleBooleanProperty runningProperty() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running.set(running);
    }

    public GlobalConfig getConfig() {
        return config.get();
    }

    public SimpleObjectProperty<GlobalConfig> configProperty() {
        return config;
    }

    public void setConfig(GlobalConfig config) {
        this.config.set(config);
    }
}
