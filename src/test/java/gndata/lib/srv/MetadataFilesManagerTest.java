package gndata.lib.srv;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


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
        if (Files.exists(tmpPath)) {
            FileUtils.deleteDirectory(tmpPath.toFile());
        }
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
