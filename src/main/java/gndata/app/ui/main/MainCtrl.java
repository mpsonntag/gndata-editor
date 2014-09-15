package gndata.app.ui.main;

import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainCtrl implements Initializable {

    public BorderPane view;
    @Inject
    private MenuView menuView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            view.setTop(menuView.getScene());
        } catch (IOException e) {
            // TODO show error dialog and quit program
            e.printStackTrace();
        }
    }
}
