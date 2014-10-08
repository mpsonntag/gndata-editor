package gndata.lib.util;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;


/**
 * A static class to build fake RDF models for tests.
 */
public class FakeRDFModel {

    private static final String schemaPath = "resources/foaf_schema.rdf";
    private static final String dataPath = "resources/foaf_example.rdf";

    private static Model loadModelFromResources(String path) {
        ClassLoader cl = FakeRDFModel.class.getClassLoader();

        return RDFDataMgr.loadModel(cl.getResource(path).toString());
    }

    public static OntModel getFakeSchema () {
        OntModel schema = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM);

        schema.addSubModel(loadModelFromResources(schemaPath));
        return schema;
    }

    public static Model getFakeAnnotations () {
        return loadModelFromResources(dataPath);
    }
}
