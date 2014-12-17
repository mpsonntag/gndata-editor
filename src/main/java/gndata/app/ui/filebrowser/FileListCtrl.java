package gndata.app.ui.filebrowser;

import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import javax.inject.Inject;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import gndata.app.state.*;
import gndata.app.ui.util.*;
import gndata.lib.srv.LocalFile;

/**
 * Controller for {@link FileListView}
 */
public class FileListCtrl implements Initializable {

    @FXML
    private ListView<LocalFile> fileList;
    @FXML
    private TextField fileFilter;

    private final ProjectState projectState;
    private final FileNavigationState navState;
    private final StringProperty filter;
    private final ObservableList<LocalFile> filteredList;
    private final List<LocalFile> unfilteredList;

    @Inject
    public FileListCtrl(ProjectState projectState, FileNavigationState navState) {
        this.projectState = projectState;
        this.navState = navState;
        filter = new SimpleStringProperty();
        filteredList = FXCollections.observableList(new ArrayList<>());
        unfilteredList = new ArrayList<>();

        this.navState.selectedParentProperty().addListener(new SelectedParentListener());
        filter.addListener((p, o, n) -> applyFilter(n));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileFilter.textProperty().bindBidirectional(filter);

        fileList.setCellFactory(cell -> new FileListCell());
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

    /**
     * If the selected parent of the navigation state has changed, the fileList
     * has to be refreshed. The previous fileList is cleared and replaced
     * by all files and folders that the current selected item contains.
     * Excluded are hidden folders and files as well as the metadata
     * folder containing rdf and ontology files.
     * Any filters that have been set before are cleared.
     */
    private class SelectedParentListener implements ChangeListener<LocalFile> {

        @Override
        public void changed(ObservableValue<? extends LocalFile> observable, LocalFile oldValue, LocalFile newValue) {
            if (newValue == null || oldValue == newValue || ! newValue.isDirectory())
                return;

            unfilteredList.clear();
            List<LocalFile> localList = new ArrayList<>();
            localList.addAll(newValue.getChildren());

            // filter hidden files and directories, exclude directory containing the actual metadata and ontology files
            //unfilteredList.stream()
            localList.stream()
                    .filter(fa -> !fa.isHidden())
                    .filter(fa -> !fa.hasPath(Paths.get(projectState.getConfig().getProjectPath(),"metadata")))
                    .forEach(unfilteredList::add);

            // reset filter
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
            LocalFile fa = fileList.getSelectionModel().getSelectedItem();
            if (fa == null || ! fa.isDirectory()) {
                return;
            }
            navState.setSelectedParent(fa);
        }
    }

    private class FileListCell extends TwoLineListCell<LocalFile> {

        @Override
        protected void update(LocalFile item, boolean empty) {
            // reset cells
            lineOne.setValue("");
            lineTwo.setValue("");
            icon.set(null);

            if ( !empty ) {
                lineOne.setValue(item.getFileName());
                lineTwo.setValue(item.getInfo());
                icon.set(item.getIcon());
            }
        }
    }
}
