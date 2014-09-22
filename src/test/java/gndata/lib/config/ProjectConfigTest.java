package gndata.lib.config;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class ProjectConfigTest {

    ProjectConfig conf;
    Path tmpPath;

    @Before
    public void setUp() throws Exception {
        tmpPath = Paths.get(System.getProperty("java.io.tmpdir"), "test-project");
    }

    @After
    public void tearDown() throws Exception {
        if (Files.exists(tmpPath)) {
            FileUtils.deleteDirectory(tmpPath.toFile());
        }
    }

    @Test
    public void testLoadStore() throws Exception {
        conf = ProjectConfig.load(tmpPath.toString());
        assert(conf.getName() == null);
        assert(conf.getDescription() == null);

        Path absPath  = Paths.get(tmpPath.toString());

        String oPath = absPath.resolve(ProjectConfig.ONTOLOGY_PATH).toString();
        assert(conf.getDefaultOntologyPath().equals(oPath));
        String cPath = absPath.resolve(ProjectConfig.CUSTOM_ONT_PATH).toString();
        assert(conf.getCustomOntologyPath().equals(cPath));
        String mPath = absPath.resolve(ProjectConfig.METADATA_PATH).toString();
        assert(conf.getMetadataStoragePath().equals(mPath));

        conf.setName("myName");
        conf.setDescription("myDescription");
        conf.store();
        conf = ProjectConfig.load(tmpPath.toString());
        assertEquals(conf.getName(), "myName");
        assertEquals(conf.getDescription(), "myDescription");
    }

}