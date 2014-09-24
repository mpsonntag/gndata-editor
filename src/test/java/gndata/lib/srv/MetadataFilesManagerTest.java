package gndata.lib.srv;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


public class MetadataFilesManagerTest {

    Path tmpPath;
    MetadataFilesManager metaMgr;

    @Before
    public void setUp() throws Exception {
        tmpPath = Paths.get(System.getProperty("java.io.tmpdir"), "foo");
        metaMgr = new MetadataFilesManager(tmpPath.toString());
    }

    @After
    public void tearDown() throws Exception {
        Stream<Path> s = Files.walk(tmpPath);
        s.forEach(a -> a.toFile().delete());

        Files.deleteIfExists(tmpPath);
    }

    @Test
    public void testInitSchemas() throws Exception {
        assert(!Files.exists(tmpPath.resolve(MetadataFilesManager.SCHEMAS_FOLDER)));

        List<Path> paths = metaMgr.schemaPaths();

        assert(Files.exists(tmpPath.resolve(MetadataFilesManager.SCHEMAS_FOLDER)));
        assert(paths.size() > 2);  // gnode, prov and custom
        for (Path p : paths) {
            assert(Files.exists(p));
        }
    }

    @Test
    public void testInitAnnotations() throws Exception {
        assert(!Files.exists(tmpPath.resolve(MetadataFilesManager.ANNOTATIONS_FOLDER)));

        Path path = metaMgr.annotationsPath();

        assert(Files.exists(tmpPath.resolve(MetadataFilesManager.ANNOTATIONS_FOLDER)));
        assert(Files.exists(path));
    }
}
