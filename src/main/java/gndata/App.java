package gndata;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.google.inject.*;
import gndata.app.state.AppState;
import gndata.app.ui.main.MainView;
import gndata.lib.config.GlobalConfig;


public class App extends Application {

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

    public static void main(String[] args) {
        launch(args);
    }

}
