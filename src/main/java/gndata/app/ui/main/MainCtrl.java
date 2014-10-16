package gndata.app.ui.main;

import gndata.app.ui.tree.MetadataTreeView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainCtrl implements Initializable {

    @FXML
    private SplitPane splitPane;

    @FXML
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
            splitPane.getItems().add(metadataView.getScene());
            splitPane.getItems().add(new HBox()); // dummy HBox

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
