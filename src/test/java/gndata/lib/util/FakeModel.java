package gndata.lib.util;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import org.apache.jena.riot.RDFDataMgr;


/**
 * A static class to build fake RDF model for tests.
 */
public class FakeModel {

    private static final String schemaPath = "resources/foaf_schema.rdf";
    private static final String dataPath = "resources/foaf_example.rdf";

    public static InfModel getFakeModel () {
        ClassLoader cl = FakeModel.class.getClassLoader();

        Model schema = RDFDataMgr.loadModel(cl.getResource(schemaPath).toString());
        Model data = RDFDataMgr.loadModel(cl.getResource(dataPath).toString());

        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);

        return ModelFactory.createInfModel(reasoner, data);
    }
}
