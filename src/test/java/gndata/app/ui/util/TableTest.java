package gndata.app.ui.util;

import com.hp.hpl.jena.rdf.model.*;
import gndata.app.ui.util.StatementTableItem;
import gndata.lib.util.FakeRDFModel;
import org.junit.Test;


public class TableTest {

    private final Model model = FakeRDFModel.getFakeAnnotations();

    @Test
    public void testTableItem() throws Exception {
        Resource tbl = model.getResource(FakeRDFModel.tbl);
        Property p = model.getProperty(model.getNsPrefixURI("foaf"), "name");
        Statement st = tbl.getProperty(p);

        StatementTableItem t = new StatementTableItem(st);

        assert(t.getPredicate().equals("name"));
        assert(t.getLiteral().equals("Tim Berners-Lee"));
    }
}
