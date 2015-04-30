package gndata.lib.util;


import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.*;
import gndata.lib.util.change.*;
import gndata.test.ThrowableAssert;
import org.junit.*;

public class OldChangeTest {

    private Model model;
    private OntModel ontology;

    private String foaf = "http://xmlns.com/foaf/0.1/";

    @Before
    public void setUp() throws Exception {
        ontology = FakeRDFModel.getFakeSchema();
        model = FakeRDFModel.getFakeAnnotations();
    }

    @Test
    public void testCreate() throws Exception {
        Model foo = FakeRDFModel.createInstance("Person", "name", "foo@bar.com");

        // merge correct
        Insert op1 = new Insert(foo);
        op1.applyTo(model, ontology);

        assert(op1.hasChanges());
        assert(model.containsAll(foo));

        // validation with reasoner
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner.bindSchema(ontology);
        InfModel infm = ModelFactory.createInfModel(reasoner, model);

        assert(infm.validate().isValid());

        // merge empty
        Insert op2 = new Insert(ModelFactory.createDefaultModel());

        ThrowableAssert.assertThat(() -> op2.applyTo(model, ontology))
                .wasThrowing(IllegalStateException.class);
        assert(!op2.hasChanges());

        // merge incorrect
        Model bar = FakeRDFModel.createInstance("NONEXIST", "name", "foo@bar.com");

        // TODO when validations implemented
    }

    @Test
    public void testDelete() throws Exception {
        OldChange op1 = new Delete(FakeRDFModel.rhm);
        op1.applyTo(model, ontology);

        assert(op1.hasChanges());
        assert(!model.containsResource(model.getResource(FakeRDFModel.rhm)));
    }
}
