package gndata.app.ui.filebrowser;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
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

/**
 * Created by msonntag on 02.12.14.
 */
public class FileListCtrl implements Initializable {

    @FXML
    private ListView<Path> fileList;

    @FXML
    private TextField fileFilter;

    private final FileNavigationState navState;
    private final StringProperty filter;
    private final ObservableList<Path> filteredList;
    private final List<Path> unfilteredList;

    @Inject
    public FileListCtrl(FileNavigationState navState) {
        this.navState = navState;
        filter = new SimpleStringProperty();
        filteredList = FXCollections.observableList(new ArrayList<Path>());
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
                    .filter(p -> p.getFileName().toString().contains(filter))
                    .forEach(p -> filteredList.add(p));
        }
    }

    private class SelectedParentListener implements ChangeListener<Path> {

        @Override
        public void changed(ObservableValue<? extends Path> observable, Path oldValue, Path newValue) {
            if (newValue == null || oldValue == newValue) {
                return;
            }
            File file = newValue.toFile();
            if (! file.exists() || ! file.isDirectory()) {
                return;
            }

            List<File> children;
            children = Arrays.asList(file.listFiles());

            unfilteredList.clear();
            children.stream()
                    .sorted(new FileComparator())
                    .map(f -> f.toPath())
                    .forEach(p -> unfilteredList.add(p));

            String currentFilter = filter.get();
            if (currentFilter == null || currentFilter.equals("")) {
                applyFilter("");
            } else {
                filter.set("");
            }
        }
    }

    private class FileComparator implements Comparator<File> {

        @Override
        public int compare(File o1, File o2) {
            if (o1.isDirectory() && ! o2.isDirectory()) {
                return -1;
            } else if ( ! o1.isDirectory() && o2.isDirectory()) {
                return 1;
            } else {
                return o1.getName().compareTo(o2.getName());
            }
        }
    }

    private class ListNavigationHandler extends DoubleClickHandler {

        @Override
        public void handleDoubleClick(MouseEvent mouseEvent) {
            Path p = fileList.getSelectionModel().getSelectedItem();
            // TODO check for directory
            navState.setSelectedParent(p);
        }
    }
}
