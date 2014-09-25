package gndata.lib.srv;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.ValidityReport;
import gndata.lib.util.FakeModel;
import org.apache.jena.riot.RDFDataMgr;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;

public class MetadataServiceTest {

    MetadataService service;

    @Before
    public void setUp() throws Exception {
        InfModel model = FakeModel.getFakeModel();

        service = new MetadataService(model);
    }

    @Test
    public void testGet() throws Exception {
        assertNotNull(service.getModel());
    }

    @Test
    public void testCreate() throws Exception {
        Path tmpPath = Paths.get(System.getProperty("java.io.tmpdir"), "foo");

        MetadataService ms = MetadataService.create(tmpPath.toString());
        InfModel model = ms.getModel();

        ValidityReport validity = model.validate();
        assert(validity.isValid());
        assert(validity.isClean());

        String pathLocal = "/resources/templates/" + MetadataFilesManager.SCHEMAS_PROV_FILE.toString();
        Model prov = RDFDataMgr.loadModel(getClass().getResource(pathLocal).toString());

        Model diff = prov.intersection(model);
        StmtIterator iter = diff.listStatements();
        while (iter.hasNext()) {
            Statement st = iter.nextStatement();
            System.out.println(st.getSubject().toString() + " with predicate: " + st.getPredicate().toString() + ", Object: " + st.getObject().toString());
        }

        iter = prov.listStatements();
        Statement st = iter.nextStatement();
        assert(model.contains(st));

        //assert(model.containsAll(prov));
    }
}
