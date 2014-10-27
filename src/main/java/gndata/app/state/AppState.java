// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.state;

import javafx.beans.property.*;

import com.google.inject.Singleton;
import gndata.lib.config.GlobalConfig;

/**
 * Class representing different basic states of the application.
 * The class can be used (along with guice) tho share state information between
 * parts of the application.
 */
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

    public void setRunning(boolean running) {
        this.running.set(running);
    }

    public SimpleBooleanProperty runningProperty() {
        return running;
    }

    public GlobalConfig getConfig() {
        return config.get();
    }

    public void setConfig(GlobalConfig config) {
        this.config.set(config);
    }

    public SimpleObjectProperty<GlobalConfig> configProperty() {
        return config;
    }
}
