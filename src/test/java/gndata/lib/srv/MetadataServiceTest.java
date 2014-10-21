package gndata.lib.srv;

import java.nio.file.*;

import static org.junit.Assert.assertNotNull;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.*;
import com.hp.hpl.jena.vocabulary.*;
import gndata.lib.util.FakeRDFModel;
import org.apache.commons.io.FileUtils;
import org.junit.*;


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

    @Test
    public void testAnnotations() throws Exception {
        Model annotations = service.getAnnotations("Tim");

        assert(annotations.listStatements().hasNext());

        Resource tbl_resource = annotations.getResource(FakeRDFModel.tbl);
        assert(annotations.contains(tbl_resource, RDF.type));

        Resource rhm_resource = annotations.getResource(FakeRDFModel.rhm);
        assert(!annotations.contains(rhm_resource, RDF.type));
    }

    @Test
    public void testReasoner() throws Exception {
        Reasoner r = service.getReasoner();

        assert(r.supportsProperty(RDFS.subClassOf));
        assert(r.supportsProperty(OWL.disjointWith));

        // TODO write more tests when Reasoner is used
    }
}
