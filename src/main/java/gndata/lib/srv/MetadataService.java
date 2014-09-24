package gndata.lib.srv;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import org.apache.jena.riot.RDFDataMgr;

import java.io.IOException;
import java.nio.file.Path;


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
     * Creates a new Metadata Service using a given path. Combines existing
     * project RDF schemas and metadata storage into a common Model. Creates
     * default schemas if some do not exist.
     *
     * @return          MetadataService
     */
    public static MetadataService create(String projectPath) throws IOException {
        MetadataFilesManager metaFiles = new MetadataFilesManager(projectPath);

        Model data = RDFDataMgr.loadModel(metaFiles.annotationsPath().toString());

        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        for (Path p : metaFiles.schemaPaths()) {
            reasoner = reasoner.bindSchema(RDFDataMgr.loadModel(p.toString()));
        }

        return new MetadataService(ModelFactory.createInfModel(reasoner, data));
    }
}
