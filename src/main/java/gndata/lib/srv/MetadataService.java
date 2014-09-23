package gndata.lib.srv;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import gndata.lib.config.ProjectConfig;
import org.apache.jena.riot.RDFDataMgr;


/**
 * Class implementing main functions working with project metadata
 */
public class MetadataService {

    private InfModel model;

    public MetadataService(InfModel m) {
        model = m;
    }

    /**
     * Returns a common RDF Model instance to access all metadata triples
     * and ontology terms.
     *
     * @return  RDF Model
     */
    public Model getModel() {
        return model;
    }

    /**
     * Creates a new Metadata Service from a given project configuration
     * by combining existing project ontology(ies) and RDF metadata
     *
     * @param config    an actual Project configuration
     * @return          MetadataService
     */
    public static MetadataService create(ProjectConfig config) {
        Model schemaC = RDFDataMgr.loadModel(config.getCustomSchemaPath());
        Model schemaD = RDFDataMgr.loadModel(config.getDefaultSchemaPath());
        Model data = RDFDataMgr.loadModel(config.getMetadataPath());

        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schemaD);
        reasoner = reasoner.bindSchema(schemaC);

        InfModel model = ModelFactory.createInfModel(reasoner, data);

        return new MetadataService(model);
    }

}
