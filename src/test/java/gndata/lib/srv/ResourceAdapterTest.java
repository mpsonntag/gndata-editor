package gndata.lib.srv;

import java.util.*;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import gndata.lib.util.*;
import org.junit.*;

/**
 * Created by andrey on 21.05.15.
 */
public class ResourceAdapterTest {

    private OntModel ontology;
    private Model model;

    private Resource robert;
    private Resource tim;
    private Resource amy;
    private Resource dean;
    private ResourceAdapter robertRA;
    private ResourceAdapter timRA;
    private ResourceAdapter amyRA;
    private ResourceAdapter deanRA;

    private final static String foaf = "http://xmlns.com/foaf/0.1/";

    @Before
    public void setUp() throws Exception {
        ontology = FakeRDFModel.getFakeSchema();
        model = FakeRDFModel.getFakeAnnotations();

        robert = model.getResource(FakeRDFModel.rhm);
        robertRA = new ResourceAdapter(robert);
        tim = model.getResource(FakeRDFModel.tbl);
        timRA = new ResourceAdapter(tim);
        amy = model.getResource("http://www.w3.org/People/Berners-Lee/card#amy");
        amyRA = new ResourceAdapter(amy);
        dean = model.getResource("http://dig.csail.mit.edu/2008/webdav/timbl/foaf.rdf#dj");
        deanRA = new ResourceAdapter(dean);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testHashCode() throws Exception {
        assert robert.hashCode() == robertRA.hashCode();
    }

    @Test
    public void testGetResource() throws Exception {
        assert robertRA.getResource().equals(robert);
    }

    @Test
    public void testToNameString() throws Exception {
        assert !robertRA.toNameString().isEmpty();

        // TODO make more extensive testing
    }

    @Test
    public void testToInfoString() throws Exception {
        assert !robertRA.toInfoString().isEmpty();

        // TODO make more extensive testing
    }

    @Test
    public void testGetLabel() throws Exception {
        assert !robertRA.getLabel().isPresent();
        assert amyRA.getLabel().isPresent();
        assert amyRA.getLabel().get().equals("Amy van der Hiel");
    }

    @Test
    public void testGetLiterals() throws Exception {
        assert timRA.getLiterals().stream()
                .filter(st -> !st.getObject().isLiteral())
                .count() == 0;

        assert timRA.getLiterals().stream()
                .filter(st -> st.getPredicate().getURI().equals(foaf + "knows"))
                .count() == 0;

        assert timRA.getLiterals().stream()
                .filter(st -> st.getPredicate().getURI().equals(foaf + "name"))
                .count() > 0;
    }

    @Test
    public void testGetResources() throws Exception {
        assert timRA.getResources().contains(robertRA);
    }

    @Test
    public void testAvailableToAdd() throws Exception {
        ObjectProperty knows = ontology.getObjectProperty(foaf + "knows");
        OntClass person = ontology.getOntClass(foaf + "Person");

        assert timRA.availableToAdd(knows, person).size() == 1;
        assert timRA.availableToAdd(knows, person).contains(deanRA);
        assert deanRA.availableToAdd(knows, person).size() > 1;
        assert deanRA.availableToAdd(knows, person).contains(timRA);

        // TODO test also functional properties
    }

    @Test
    public void testRemoveObjectProperties() throws Exception {
        List<Resource> toRemove = new ArrayList<>();
        toRemove.add(robert);

        assert timRA.getResources().contains(robertRA);
        assert timRA.getResources().contains(amyRA);

        timRA.removeObjectProperties(toRemove);

        assert !timRA.getResources().contains(robertRA);
        assert timRA.getResources().contains(amyRA);
    }

    @Test
    public void testRemove() throws Exception {
        robertRA.remove();

        assert model.listStatements().toList().stream()
                .filter(st -> st.getSubject().equals(robert)).count() == 0;
        assert !timRA.getResources().contains(robertRA);
    }
}
