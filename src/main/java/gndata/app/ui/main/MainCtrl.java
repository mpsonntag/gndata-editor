package gndata.app.ui.main;

import gndata.app.ui.metadata.TreeCtrl;
import gndata.app.ui.metadata.TreeView;
import gndata.app.ui.metadata.table.TableCtrl;
import gndata.app.ui.metadata.table.TableView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

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
    private TreeView metadataView;

    @Inject
    private TableView tableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // top Menu bar
            view.setTop(menuView.getScene());

            // split pane with metadata tree
            splitPane.getItems().add(metadataView.getScene());
            splitPane.getItems().add(tableView.getScene());

            TreeCtrl treeCtrl = metadataView.getLoader().getController();
            TableCtrl tableCtrl = tableView.getLoader().getController();

            // listener to update the table after metadata item selection
            tableCtrl.bindTo(treeCtrl.getTree());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
