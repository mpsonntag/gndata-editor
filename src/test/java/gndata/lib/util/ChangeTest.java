package gndata.lib.util;


import java.util.UUID;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.*;
import com.hp.hpl.jena.vocabulary.RDF;
import gndata.lib.util.change.Create;
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
        // create new fake object
        Model new_object = ModelFactory.createDefaultModel();

        Resource res = ResourceFactory.createResource(UUID.randomUUID().toString());

        Property name = ontology.getOntProperty(foaf + "name");
        Property mbox = ontology.getOntProperty(foaf + "mbox");

        new_object.add(res, RDF.type, foaf + "Person");
        new_object.add(res, name, "Foo");
        new_object.add(res, mbox, "foo@bar.com");

        Create op = new Create(new_object, ontology);
        op.applyTo(model);

        assert(op.applied());
        assert(model.containsAll(new_object));

        // validation with reasoner
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner.bindSchema(ontology);
        InfModel infm = ModelFactory.createInfModel(reasoner, model);

        assert(infm.validate().isValid());
    }

    @Test
    public void testDelete() throws Exception {
        // TODO implement
    }
}
