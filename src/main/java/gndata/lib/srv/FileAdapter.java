package gndata.lib.srv;


import java.util.*;


/**
 * Created by msonntag on 04.12.14.
 */
public abstract class FileAdapter<T extends FileAdapter> implements Comparable<T> {

    public abstract Optional<T> getParent();

    /**
     *
     * @return sorted list of child FileAdapters
     */
    public abstract List<T> getChildren();

    public abstract boolean isDirectory();

    public abstract String getFileName();

    public boolean hasChild(T child) {

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
    public int compareTo(T o) {
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
