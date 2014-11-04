package gndata.app.ui.metadata;

import com.hp.hpl.jena.rdf.model.*;
import gndata.lib.util.FakeRDFModel;
import org.junit.Test;


public class VisualItemTest {

    private final String w3c = "http://www.w3.org/data#W3C";
    private final Model model = FakeRDFModel.getFakeAnnotations();

    @Test
    public void testRenderResource() throws Exception {
        Resource r = model.getResource(w3c);
        assert(VisualItem.renderForSorting(r).contains("W3C"));  // contains label

        Resource t = model.getResource(FakeRDFModel.tbl);
        assert(VisualItem.renderForSorting(t).contains("Person"));  // contains type name
    }
}
