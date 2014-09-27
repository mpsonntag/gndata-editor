package gndata.lib.srv;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.reasoner.ValidityReport;
import gndata.lib.util.FakeModel;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;


public class MetadataServiceTest {

    private static final Path tmpPath = Paths.get(System.getProperty("java.io.tmpdir"), "test-project");
    MetadataService service;

    @Before
    public void setUp() throws Exception {
        InfModel model = FakeModel.getFakeModel();

        service = new MetadataService(model);
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
        MetadataService ms = MetadataService.create(tmpPath.toString());
        InfModel model = ms.getModel();

        ValidityReport validity = model.validate();
        assert(validity.isValid());
        assert(validity.isClean());
    }
}
