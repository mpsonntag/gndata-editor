package gndata.lib.srv;

import java.nio.file.Path;
import javafx.beans.property.*;

/**
 * Created by msonntag on 03.12.14.
 */
public class FileService {

    private final ObjectProperty<Path> root;

    public FileService(Path root) {
        this.root = new SimpleObjectProperty<>(root);
    }

    public Path getRoot() {
        return root.get();
    }

    public boolean isFileInProject(Path path) {

        // TODO implement check
        return true;

    }

    public boolean isFileInProject(FileAdapter path) {

        // TODO implement check
        return true;

    }

}
