package gndata.lib.util;


import static gndata.app.ui.util.NameConventions.templateResource;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.*;
import gndata.lib.util.change.*;
import gndata.test.ThrowableAssert;
import org.junit.*;

public class ChangeTest {

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
        Merge op1 = new Merge(foo, ontology);
        op1.applyTo(model);

        assert(op1.applied());
        assert(model.containsAll(foo));

        // validation with reasoner
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner.bindSchema(ontology);
        InfModel infm = ModelFactory.createInfModel(reasoner, model);

        assert(infm.validate().isValid());

        // merge incorrect
        Model bar = FakeRDFModel.createInstance("NONEXIST", "name", "foo@bar.com");
        Merge op2 = new Merge(bar, ontology);

        ThrowableAssert.assertThat(() -> op2.applyTo(model))
                .wasThrowing(AssertionError.class);
        assert(!op2.applied());
    }

    @Test
    public void testDelete() throws Exception {
        Change op1 = new Delete(FakeRDFModel.rhm, true);
        op1.applyTo(model);

        assert(op1.applied());
        assert(!model.containsResource(model.getResource(FakeRDFModel.rhm)));
    }
}
