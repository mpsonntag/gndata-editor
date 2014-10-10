package gndata.app.ui.main;

import gndata.app.ui.tree.MetadataTreeView;
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

    @Inject
    private MetadataTreeView metadataView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            view.setTop(menuView.getScene());
            view.setLeft(metadataView.getScene());
        } catch (IOException e) {
            // TODO show error dialog and quit program
            e.printStackTrace();
        }
    }
}
