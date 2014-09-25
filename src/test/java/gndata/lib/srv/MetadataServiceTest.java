package gndata.lib.srv;

import com.hp.hpl.jena.rdf.model.InfModel;
import gndata.lib.config.ProjectConfig;
import gndata.lib.util.FakeModel;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class MetadataServiceTest {

    private static final Path tmpPath = Paths.get(System.getProperty("java.io.tmpdir"), "test-project");

    MetadataService service;
    ProjectConfig config;

    @Before
    public void setUp() throws Exception {
        InfModel model = FakeModel.getFakeModel();

        service = new MetadataService(model);
        config  = ProjectConfig.load(tmpPath.toString());
    }

    @After
    public void tearDown() throws Exception {
        if (Files.exists(tmpPath)) {
            FileUtils.deleteDirectory(tmpPath.toFile());
        }
    }

    @Test
    public void testGet() throws Exception {
        assertNotNull(service.getModel());
    }

    @Test
    public void testCreate() throws Exception {
        // TODO implement test
        //service = MetadataService.create(config);
        assertNotNull(service);
    }
}
