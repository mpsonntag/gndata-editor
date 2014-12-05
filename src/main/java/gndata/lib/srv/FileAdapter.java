package gndata.lib.srv;

import java.nio.file.Files;
import java.util.*;

/**
 * Created by msonntag on 04.12.14.
 */
public abstract class FileAdapter implements Comparable<FileAdapter> {

    public abstract Optional<FileAdapter> getParent();

    /**
     *
     * @return sorted list of child FileAdapters
     */
    public abstract List<FileAdapter> getChildren();

    public abstract boolean isDirectory();

    public abstract String getFileName();

    public boolean hasChild(FileAdapter child) {
        if ( ! isDirectory() ) {
            return false;
        }

        return getChildren().contains(child);
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

    @Override
    public abstract boolean equals(Object obj);
}