package gndata;

import com.google.inject.Guice;
import com.google.inject.Injector;
import gndata.app.ui.main.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception
    {
        //AquaFx.style();
        setUserAgentStylesheet(STYLESHEET_MODENA);

        Injector injector = Guice.createInjector(new AppModule());

        MainView main = injector.getInstance(MainView.class);
        Scene scene = new Scene(main.getScene());

        AppState state = injector.getInstance(AppState.class);
        state.runningProperty().addListener((observable, oldVal, newVal) -> {
            if (!newVal) {
                stage.close();
            }
        });

        stage.setTitle("Address Book");
        stage.setScene(scene);
        stage.show();
    }

    public static void main( String[] args ) {
        launch(args);
    }

}
