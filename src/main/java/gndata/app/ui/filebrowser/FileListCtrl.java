package gndata.app.ui.filebrowser;

import java.net.URL;
import java.util.*;
import javax.inject.Inject;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import gndata.app.state.FileNavigationState;
import gndata.app.ui.util.DoubleClickHandler;
import gndata.lib.srv.FileAdapter;

/**
 * Created by msonntag on 02.12.14.
 */
public class FileListCtrl implements Initializable {

    @FXML
    private ListView<FileAdapter> fileList;

    @FXML
    private TextField fileFilter;

    private final FileNavigationState navState;
    private final StringProperty filter;
    private final ObservableList<FileAdapter> filteredList;
    private final List<FileAdapter> unfilteredList;

    @Inject
    public FileListCtrl(FileNavigationState navState) {
        this.navState = navState;
        filter = new SimpleStringProperty();
        filteredList = FXCollections.observableList(new ArrayList<FileAdapter>());
        unfilteredList = new ArrayList<>();

        this.navState.selectedParentProperty().addListener(new SelectedParentListener());
        filter.addListener((p, o, n) -> applyFilter(n));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileFilter.textProperty().bindBidirectional(filter);
        fileList.setItems(filteredList);

        navState.selectedFileProperty().bind(fileList.getSelectionModel().selectedItemProperty());
        fileList.addEventHandler(MouseEvent.MOUSE_CLICKED, new ListNavigationHandler());
    }

    public void applyFilter(String filter) {
        filteredList.clear();

        if (filter == null || filter.equals("")) {
            filteredList.addAll(unfilteredList);

        } else {
            unfilteredList.stream()
                    .filter(fa -> fa.getFileName().contains(filter))
                    .forEach(fa -> filteredList.add(fa));
        }
    }

    private class SelectedParentListener implements ChangeListener<FileAdapter> {

        @Override
        public void changed(ObservableValue<? extends FileAdapter> observable, FileAdapter oldValue, FileAdapter newValue) {
            if (newValue == null || oldValue == newValue) {
                return;
            }

            if (! newValue.isDirectory()) {
                return;
            }
            unfilteredList.clear();
            unfilteredList.addAll(newValue.getChildren());

            String currentFilter = filter.get();
            if (currentFilter == null || currentFilter.equals("")) {
                applyFilter("");
            } else {
                filter.set("");
            }
        }
    }

    private class ListNavigationHandler extends DoubleClickHandler {

        @Override
        public void handleDoubleClick(MouseEvent mouseEvent) {
            FileAdapter fa = fileList.getSelectionModel().getSelectedItem();
            if (! fa.isDirectory()) {
                return;
            }
            navState.setSelectedParent(fa);
        }
    }
}
