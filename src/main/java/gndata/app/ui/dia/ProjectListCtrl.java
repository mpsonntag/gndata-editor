package gndata.app.ui.dia;

import gndata.app.ui.util.DialogController;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller for the {@link ProjectListView}.
 */
public class ProjectListCtrl extends DialogController<String> implements Initializable {

    @FXML
    private BorderPane view;
    @FXML
    private ListView<String> list;

    private ObservableList<String> items;

    public ProjectListCtrl(Map<String, String> projects) {
        this.items = new SimpleListProperty<>();
        this.items.addAll(projects.keySet());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.setItems(items);
    }

    @Override
    public String getResult() {
        return null;
    }

    @Override
    public Node getView() {
        return view;
    }
}
