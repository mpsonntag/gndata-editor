package gndata.lib.srv;

import com.hp.hpl.jena.rdf.model.InfModel;
import gndata.lib.util.FakeModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
}
