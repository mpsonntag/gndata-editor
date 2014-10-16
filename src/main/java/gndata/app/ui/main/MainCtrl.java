package gndata.app.ui.main;

import gndata.app.ui.tree.MetadataTreeView;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

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
            // top Menu bar
            view.setTop(menuView.getScene());

            // split pane with metadata tree
            SplitPane s = (SplitPane) view.getCenter();
            s.getItems().add(metadataView.getScene());

            HBox rightArea = new HBox();  // dummy HBox
            s.getItems().add(rightArea);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
