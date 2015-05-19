package gndata.lib.util;

import java.util.*;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.RDF;
import org.apache.jena.riot.RDFDataMgr;


/**
 * A static class to build fake RDF models for tests.
 */
public class FakeRDFModel {

    private static final String schemaPath = "testfiles/foaf_schema.rdf";
    private static final String dataPath = "testfiles/foaf_example.rdf";

    public static String tbl = "http://www.w3.org/People/Berners-Lee/card#i";
    public static String rhm = "http://dig.csail.mit.edu/2007/wiki/people/RobertHoffmann#RMH";

    private static Model loadModelFromResources(String path) {
        ClassLoader cl = FakeRDFModel.class.getClassLoader();

        return RDFDataMgr.loadModel(cl.getResource(path).toString());
    }

    public static OntModel getFakeSchema() {
        OntModel schema = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF);

        schema.addSubModel(loadModelFromResources(schemaPath));
        return schema;
    }

    public static Model getFakeAnnotations() {
        return loadModelFromResources(dataPath);
    }

    public static Model createInstance(String cls, String name, String mbox) {
        OntModel ontology = getFakeSchema();
        Map<String, String> prefixes = ontology.getNsPrefixMap();
        String foaf = prefixes.get("foaf");

        Model new_object = ModelFactory.createDefaultModel();

        Resource res = ResourceFactory.createResource(UUID.randomUUID().toString());

        new_object.add(res, RDF.type, foaf + cls);
        new_object.add(res, ontology.getOntProperty(foaf + "name"), name);
        new_object.add(res, ontology.getOntProperty(foaf + "mbox"), mbox);

        return new_object;
    }
}
