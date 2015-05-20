package gndata.lib.util;

import java.util.*;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.*;


public class OntologyHelperTest {

    private OntModel ontology;
    private Model model;
    private OntologyHelper hlp;

    private final static String foaf = "http://xmlns.com/foaf/0.1/";

    @Before
    public void setUp() throws Exception {
        ontology = FakeRDFModel.getFakeSchema();
        model = FakeRDFModel.getFakeAnnotations();

        hlp = new OntologyHelper(ontology);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testListClasses() throws Exception {
        Set<OntClass> lst;
        OntClass person = ontology.getOntClass(foaf + "Person");
        OntClass group = ontology.getOntClass(foaf + "Group");

        lst = hlp.listClasses();

        assert lst.contains(person);
        assert lst.contains(group);

        lst = hlp.listClasses(model.getResource(FakeRDFModel.rhm));

        assert lst.contains(person);
        assert !lst.contains(group);
    }

    @Test
    public void testRange() throws Exception {
        OntClass person = ontology.getOntClass(foaf + "Person");
        OntClass document = ontology.getOntClass(foaf + "Document");

        ObjectProperty knows = ontology.getObjectProperty(foaf + "knows");
        ObjectProperty publications = ontology.getObjectProperty(foaf + "publications");

        assert hlp.getRange(knows).contains(person);
        assert !hlp.getRange(knows).contains(document);

        assert !hlp.getRange(publications).contains(person);
        assert hlp.getRange(publications).contains(document);

        DatatypeProperty fName = ontology.getDatatypeProperty(foaf + "firstName");

        assert hlp.getRange(fName).contains(XSDDatatype.XSDstring);

        // TODO FOAF does not seem to contain type restrictions
        // TODO add more tests
    }

    @Test
    public void testRelated() throws Exception {
        Set<Pair<ObjectProperty, OntClass>> lst;
        OntClass person = ontology.getOntClass(foaf + "Person");
        OntClass document = ontology.getOntClass(foaf + "Document");

        ObjectProperty knows = ontology.getObjectProperty(foaf + "knows");
        ObjectProperty publications = ontology.getObjectProperty(foaf + "publications");

        lst = hlp.listRelated(person);

        assert lst.contains(Pair.of(knows, person));
        assert lst.contains(Pair.of(publications, document));

        lst = hlp.listRelated(model.getResource(FakeRDFModel.rhm));

        assert lst.contains(Pair.of(knows, person));
        assert lst.contains(Pair.of(publications, document));

        assert hlp.getRange(knows).contains(person);
        assert !hlp.getRange(knows).contains(document);

        assert !hlp.getRange(publications).contains(person);
        assert hlp.getRange(publications).contains(document);
    }

    @Test
    public void testProperties() throws Exception {
        OntClass person = ontology.getOntClass(foaf + "Person");

        DatatypeProperty fName = ontology.getDatatypeProperty(foaf + "firstName");
        DatatypeProperty sha1 = ontology.getDatatypeProperty(foaf + "sha1");

        ObjectProperty knows = ontology.getObjectProperty(foaf + "knows");
        ObjectProperty thumbnail = ontology.getObjectProperty(foaf + "thumbnail");

        assert hlp.listDatatypeProperties(person).contains(fName);
        assert !hlp.listDatatypeProperties(person).contains(sha1);

        assert hlp.listObjectProperties(person).contains(knows);
        assert !hlp.listObjectProperties(person).contains(thumbnail);

        Resource res = model.getResource(FakeRDFModel.rhm);

        assert hlp.listDatatypeProperties(res).contains(fName);
        assert !hlp.listDatatypeProperties(res).contains(sha1);

        assert hlp.listObjectProperties(res).contains(knows);
        assert !hlp.listObjectProperties(res).contains(thumbnail);
    }
}
