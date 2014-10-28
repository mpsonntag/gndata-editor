// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.google.inject.*;
import gndata.app.state.AppState;
import gndata.app.ui.main.MainView;
import gndata.lib.config.GlobalConfig;

/**
 * Main application class containing the main method as program entry point.
 */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the main application.
     *
     * @param stage The main stage of the application.
     * @throws Exception Errors occurring during application runtime.
     */
    @Override
    public void start(Stage stage) throws Exception {
        //AquaFx.style();
        setUserAgentStylesheet(STYLESHEET_MODENA);

        Injector injector = Guice.createInjector(new AppModule());

        MainView main = injector.getInstance(MainView.class);
        Scene scene = new Scene(main.getScene());

        AppState state = injector.getInstance(AppState.class);
        state.setConfig(GlobalConfig.load(GlobalConfig.makeConfigPath()));
        state.runningProperty().addListener((observable, oldVal, newVal) -> {
            if (!newVal) {
                stage.close();
            }
        });

        stage.setTitle("GNData Editor");
        stage.setScene(scene);
        stage.show();
    }

}
