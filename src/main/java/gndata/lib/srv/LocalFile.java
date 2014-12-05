package gndata.lib.srv;

import java.io.File;
import java.nio.file.*;
import java.util.*;

/**
 * Created by msonntag on 04.12.14.
 */
public class LocalFile extends FileAdapter {

    private Path path;
    private Optional<FileAdapter> parent;

    public LocalFile(Path path) {
        this.path = path.toAbsolutePath().normalize();
    }

    public LocalFile(String path) {
        this(Paths.get(path));
    }

    @Override
    public Optional<FileAdapter> getParent() {
        if (parent == null) {
            parent = path.getParent() == null ? Optional.empty() : Optional.of(new LocalFile(path.getParent()));
        }

        return parent;
    }

    @Override
    public List<FileAdapter> getChildren() {
        List<FileAdapter> list = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {

            stream.forEach(p -> list.add(new LocalFile(p)));

        } catch(Exception e) {
            e.printStackTrace();
        }

        list.sort((a, b) -> a.compareTo(b));
        return list;
    }

    @Override
    public boolean isDirectory() {
        return Files.isDirectory(path);
    }

    @Override
    public String getFileName() {
        return path.getFileName().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof LocalFile))
            return false;

        LocalFile localFile = (LocalFile) o;
        return path.equals(localFile.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}
