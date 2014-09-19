package gndata.app.state;

import com.google.inject.Singleton;
import javafx.beans.property.SimpleBooleanProperty;

@Singleton
public class AppState {

    private final SimpleBooleanProperty running;

    public AppState() {
        running = new SimpleBooleanProperty(true);
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
}
