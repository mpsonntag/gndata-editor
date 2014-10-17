package gndata.app.ui.tree.files;

import javafx.scene.control.TreeCell;
import javafx.scene.paint.Color;

import java.io.File;


/**
 * Facade class for file tree items.
 */
public final class FileTreeCell extends TreeCell<File> {

    public FileTreeCell() {}

    @Override
    public void updateItem(File item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {

            setText(null);

        } else if (item != null) {

            Color clr = item.isFile() ? Color.GREEN : Color.BLACK;

            setTextFill(clr);
            setText(item.getName());
        }
    }
}
