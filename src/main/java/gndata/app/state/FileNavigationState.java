package gndata.app.state;

import java.util.ArrayList;
import javax.inject.Singleton;

import gndata.lib.srv.LocalFile;
import javafx.beans.property.*;
import javafx.collections.*;

import gndata.lib.srv.FileAdapter;

/**
 * Created by msonntag on 03.12.14.
 */
@Singleton
public final class FileNavigationState {

    private final ObjectProperty<FileAdapter> selectedParent;
    private final ObjectProperty<FileAdapter> selectedFile;
    private final ObservableList<FileAdapter> favoriteFolders;
    private final ObservableList<FileAdapter> navigationPath;

    public FileNavigationState() {
        selectedParent = new SimpleObjectProperty<>();
        selectedFile = new SimpleObjectProperty<>();

        favoriteFolders = FXCollections.observableList(new ArrayList<FileAdapter>());
        // TODO get actual file favorites from project settings instead of
        // using dummy entries

        navigationPath = FXCollections.observableList(new ArrayList<FileAdapter>());
    }

    public FileAdapter getSelectedParent() {
        return selectedParent.get();
    }

    public ObjectProperty<FileAdapter> selectedParentProperty() {
        return selectedParent;
    }

    public void setSelectedParent(FileAdapter selectedParent) {
        this.selectedParent.set(selectedParent);
    }

    public FileAdapter getSelectedFile() {
        return selectedFile.get();
    }

    public ObjectProperty<FileAdapter> selectedFileProperty() {
        return selectedFile;
    }

    public void setSelectedFile(FileAdapter selectedFile) {
        this.selectedFile.set(selectedFile);
    }

    public ObservableList<FileAdapter> getFavoriteFolders() { return favoriteFolders; }

    public ObservableList<FileAdapter> getNavigationPath() {
        return navigationPath;
    }
}
