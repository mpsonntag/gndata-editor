package gndata.app.ui.dia;

import java.net.URL;
import java.util.*;
import java.util.Map.Entry;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import gndata.app.ui.util.DialogController;

/**
 * Controller for the {@link ProjectListView}.
 */
public class ProjectListCtrl extends DialogController<String> implements Initializable {

    @FXML
    private BorderPane view;
    @FXML
    private ListView<Entry<String, String>> list;

    private Map<String, String> projects;

    public ProjectListCtrl(Map<String, String> projects) {
        this.projects = projects;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.setCellFactory(view -> new ProjectListCell());
        list.getItems().addAll(projects.entrySet());
    }

    @Override
    public String getResult() {
        SelectionModel<Entry<String, String>> sel = list.getSelectionModel();
        if (!sel.isEmpty()) {
            return sel.getSelectedItem().getKey();
        } else {
            return null;
        }
    }

    @Override
    public Node getView() {
        return view;
    }

    private class ProjectListCell extends ListCell<Entry<String, String>> {

        @Override
        protected void updateItem(Entry<String, String> content, boolean empty) {
            super.updateItem(content, empty);

            if (!empty) {
                Label head = new Label(content.getValue());
                head.setStyle("-fx-font-size: 1.2em; -fx-font-weight: bold");

                Label other = new Label(content.getKey());

                setGraphic(new VBox(head, other));
            }
        }

    }
}
