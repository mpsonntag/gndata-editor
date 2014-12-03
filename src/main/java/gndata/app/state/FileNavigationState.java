package gndata.app.state;

import java.nio.file.Path;
import java.util.ArrayList;
import javax.inject.Singleton;
import javafx.beans.property.*;
import javafx.collections.*;

/**
 * Created by msonntag on 03.12.14.
 */
@Singleton
public final class FileNavigationState {

    private final ObjectProperty<Path> selectedParent;
    private final ObjectProperty<Path> selectedFile;
    private final ObservableList<Path> favoriteFolders;
    private final ObservableList<Path> navigationPath;

    public FileNavigationState() {
        selectedParent = new SimpleObjectProperty<>();
        selectedFile = new SimpleObjectProperty<>();

        favoriteFolders = FXCollections.observableList(new ArrayList<Path>());
        navigationPath = FXCollections.observableList(new ArrayList<Path>());
    }

    public Path getSelectedParent() {
        return selectedParent.get();
    }

    public ObjectProperty<Path> selectedParentProperty() {
        return selectedParent;
    }

    public void setSelectedParent(Path selectedParent) {
        this.selectedParent.set(selectedParent);
    }

    public Path getSelectedFile() {
        return selectedFile.get();
    }

    public ObjectProperty<Path> selectedFileProperty() {
        return selectedFile;
    }

    public void setSelectedFile(Path selectedFile) {
        this.selectedFile.set(selectedFile);
    }

    public ObservableList<Path> getFavoriteFolders() {
        return favoriteFolders;
    }

    public ObservableList<Path> getNavigationPath() {
        return navigationPath;
    }
}
