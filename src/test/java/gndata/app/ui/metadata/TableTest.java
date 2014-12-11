package gndata.app.ui.metadata;

import java.util.*;

import com.hp.hpl.jena.rdf.model.*;
import gndata.app.ui.util.StatementTableItem;
import gndata.lib.util.FakeRDFModel;
import org.junit.Test;


public class TableTest {

    private final Model model = FakeRDFModel.getFakeAnnotations();

    @Test
    public void testTableCtrl() throws Exception {
        Resource tbl = model.getResource(FakeRDFModel.tbl);
        List<StatementTableItem> l = StatementTableItem.buildTableItems(tbl);

        // list should only contain literals, one 'name' item in this case
        Optional<StatementTableItem> t = l.stream().filter(a -> a.getPredicate().equals("name")).findFirst();
        assert(t.isPresent());
        assert(t.get().getLiteral().equals("Tim Berners-Lee"));

        // node=null should result in empty list
        List<StatementTableItem> empty = StatementTableItem.buildTableItems(null);
        assert(empty.size() == 0);
    }

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
