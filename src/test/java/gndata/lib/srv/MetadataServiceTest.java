package gndata.lib.srv;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.reasoner.ValidityReport;
import gndata.lib.util.FakeRDFModel;
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
        OntModel schema = FakeRDFModel.getFakeSchema();
        Model annotations = FakeRDFModel.getFakeAnnotations();

        service = new MetadataService(schema, annotations);
    }

    @After
    public void tearDown() throws Exception {
        if (Files.exists(tmpPath)) {
            FileUtils.deleteDirectory(tmpPath.toFile());
        }
    }

    @Test
    public void testGet() throws Exception {
        OntModel schema = service.getSchema();
        Model annotations = service.getAnnotations();

        assertNotNull(schema);
        assertNotNull(annotations);

        assert(schema.listStatements().hasNext());
        assert(annotations.listStatements().hasNext());
    }

    @Test
    public void testValid() throws Exception {
        ValidityReport validity = service.getAnnotationsWithInference().validate();
        assert(validity.isValid());
        assert(validity.isClean());
    }

    @Test
    public void testCreate() throws Exception {
        MetadataService ms = MetadataService.create(tmpPath.toString());
        OntModel schema = ms.getSchema();
        Model annotations = ms.getAnnotations();

        assertNotNull(schema);
        assertNotNull(annotations);
    }
}
