package gndata.lib.srv;

import java.nio.file.Files;
import java.util.*;

/**
 * Created by msonntag on 04.12.14.
 */
public abstract class FileAdapter implements Comparable<FileAdapter> {

    public abstract Optional<FileAdapter> getParent();

    public abstract List<FileAdapter> getChildren();

    public abstract boolean isDirectory();

    public abstract String getFileName();

    public boolean hasChild(FileAdapter child) {
        if ( ! isDirectory() ) {
            return false;
        }

        return getChildren().stream()
                    .filter(fa -> fa.getFileName().equals(child.getFileName()))
                    .count() > 0;
    }

    @Override
    public String toString() {
        return getFileName();
    }

    @Override
    public int compareTo(FileAdapter o) {
        if (isDirectory() && ! o.isDirectory()) {
            return -1;
        } else if ( ! isDirectory() && o.isDirectory()) {
            return 1;
        } else {
            return getFileName().compareTo(o.getFileName());
        }

    }
}