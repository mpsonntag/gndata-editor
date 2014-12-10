package gndata.lib.srv;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;

/**
 * Created by msonntag on 04.12.14.
 */
public class LocalFile extends FileAdapter<LocalFile> {

    private Path path;
    private Optional<LocalFile> parent;

    public LocalFile(Path path) {
        this.path = path.toAbsolutePath().normalize();
    }

    public LocalFile(String path) {
        this(Paths.get(path));
    }

    @Override
    public Optional<LocalFile> getParent() {
        if (parent == null) {
            parent = path.getParent() == null ? Optional.empty() : Optional.of(new LocalFile(path.getParent()));
        }

        return parent;
    }

    @Override
    public List<LocalFile> getChildren() {
        List<LocalFile> list = new ArrayList<>();
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

    public long getSizeInBytes() {
        try {
            return Files.size(path);
        } catch (IOException e) {
            // TODO insert proper exception handling
            return 0;
        }
    }

    public String getMimeType() {

        String returnString = "";

        if(!Files.isDirectory(path)) {
            TikaConfig tc = TikaConfig.getDefaultConfig();
            Detector detector = tc.getDetector();
            TikaInputStream stream = null;
            try {
                stream = TikaInputStream.get(new FileInputStream(new File(path.toString())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Metadata metadata = new Metadata();
            metadata.add(Metadata.RESOURCE_NAME_KEY, path.getFileName().toString());
            try {
                MediaType mediaType = detector.detect(stream, metadata);
                returnString = mediaType.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return returnString;
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
