package gndata.lib.srv;

import java.util.*;

/**
 * Created by andrey on 20.05.15.
 */
public interface IFileAdapter<T extends IFileAdapter> {

    public Optional<T> getParent();

    public List<T> getChildren();

    public boolean isDirectory();

    public String getFileName();

    public default boolean hasChild(T child) {
        return isDirectory() && getChildren().contains(child);
    }

    public boolean equals(Object obj);

    public default int compareTo(T o) {
        if (isDirectory() && ! o.isDirectory()) {
            return -1;
        } else if ( ! isDirectory() && o.isDirectory()) {
            return 1;
        } else {
            return getFileName().compareTo(o.getFileName());
        }
    }
}
