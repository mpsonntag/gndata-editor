package gndata.lib.util;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;
import org.junit.*;

/**
 * Tests for RDF model changes tracking.
 */
public class ChangeTest {

    private Model model;
    private OntModel ontology;

    private ChangeHelper changes;

    private String foaf = "http://xmlns.com/foaf/0.1/";

    @Before
    public void setUp() throws Exception {
        ontology = FakeRDFModel.getFakeSchema();
        model = FakeRDFModel.getFakeAnnotations();

        changes = new ChangeHelper(model, ontology);
    }

    @Test
    public void testCreate() throws Exception {
        Model foo = FakeRDFModel.createInstance("Person", "name", "foo@bar.com");

        model.add(foo);

        assert foo.difference(changes.get(0).getChange()).isEmpty();

        assert changes.size() == 1;
        assert model.containsAll(foo);

        changes.undo();

        assert changes.size() == 1;
        assert !model.containsAny(foo);

        changes.redo();

        assert changes.size() == 1;
        assert model.containsAll(foo);
    }

    @Test
    public void testDelete() throws Exception {
        Resource rhmRes = model.getResource(FakeRDFModel.rhm);

        Model toRemove = ModelFactory.createDefaultModel();
        toRemove.add(model.listStatements(rhmRes, null, (RDFNode) null));
        toRemove.add(model.listStatements(null, null, rhmRes));

        model.remove(toRemove);

        Model foo = changes.get(0).getChange();

        assert changes.size() == 1;
        assert !model.containsResource(rhmRes);

        changes.undo();

        assert changes.size() == 1;
        assert model.containsResource(rhmRes);
        assert model.containsAll(foo);

        changes.redo();

        assert changes.size() == 1;
        assert !model.containsResource(rhmRes);
    }

    @Test
    public void testUpdate() throws Exception {
        Resource rhmRes = model.getResource(FakeRDFModel.rhm);
        Property name = model.getProperty(foaf + "name");

        Statement nameSt = rhmRes.listProperties(name).nextStatement();
        String originalName = nameSt.getLiteral().getString();

        // assume update is always first remove and then add
        model.remove(nameSt);
        model.add(rhmRes, name, "TESTNAME");

        assert changes.size() == 2;
        assert rhmRes.listProperties(name).toList().size() == 1;
        assert rhmRes.listProperties(name).nextStatement().getLiteral().getString().equals("TESTNAME");

        changes.undo();

        assert changes.size() == 2;
        assert rhmRes.listProperties(name).toList().size() == 1;
        assert rhmRes.listProperties(name).nextStatement().getLiteral().getString().equals(originalName);

        changes.redo();

        assert changes.size() == 2;
        assert rhmRes.listProperties(name).toList().size() == 1;
        assert rhmRes.listProperties(name).nextStatement().getLiteral().getString().equals("TESTNAME");
    }
}