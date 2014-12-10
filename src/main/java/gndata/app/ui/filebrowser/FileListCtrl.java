package gndata.app.ui.filebrowser;

import gndata.app.state.FileNavigationState;
import gndata.app.ui.util.DoubleClickHandler;
import gndata.app.ui.util.TwoLineListCell;
import gndata.lib.srv.FileAdapter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for {@link FileListView}
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
            if (fa == null || ! fa.isDirectory()) {
                return;
            }
            navState.setSelectedParent(fa);
        }
    }

    private class FileListCell extends TwoLineListCell<FileAdapter> {

        @Override
        protected void update(FileAdapter item, boolean empty) {
            // reset cells
            lineOne.setValue("");
            lineTwo.setValue("");
            icon.set(null);

            if ( !empty ) {
                String firstLine = item.getFileName();
                String secondLine = item.getMimeType();
                String selectIcon;

                if(item.isDirectory()) {
                    firstLine += " ("+ item.getChildren().size() +")";
                    selectIcon = "folder.png";

                } else {
                    secondLine += " "+ humanReadableByteCount(item.getSizeInBytes(), true);
                    selectIcon = "txt.png";
                }
                Image img = new Image(ClassLoader.getSystemResource(new File("icons", selectIcon).toString()).toString());

                lineOne.setValue(firstLine);
                lineTwo.setValue(secondLine);
                icon.set(img);
            }
        }
    }

    /**
     * Method returns number of bytes in readable format
     *
     * @param bytes: number of bytes to be displayed in readable format
     * @param si: use true, if basis for byte conversion should be 1000, false if basis should be 1024
     * @return: Formatted String
     */
    private static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit)
            return bytes + " B";

        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");

        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

}
