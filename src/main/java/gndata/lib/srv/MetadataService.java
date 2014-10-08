package gndata.lib.srv;

import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
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
    public InfModel getModel() {
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
        if (projectPath == null) {
            throw new IOException("Cannot create metadata service at non-existing path");
        }

        MetadataFilesManager metaFiles = new MetadataFilesManager(projectPath);

        Model data = RDFDataMgr.loadModel(metaFiles.annotationsPath().toString());

        OntModel sch = ModelFactory.createOntologyModel();


        OntDocumentManager mgr = new OntDocumentManager();
        OntModelSpec s = new OntModelSpec( OntModelSpec.RDFS_MEM );
        s.setDocumentManager( mgr );
        OntModel m = ModelFactory.createOntologyModel( s );

        sch.addSubModel(m);



        Model schema = ModelFactory.createDefaultModel();
        for (Path p : metaFiles.schemaPaths()) {
            schema = schema.union(RDFDataMgr.loadModel(p.toString()));
        }

        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);

        return new MetadataService(ModelFactory.createInfModel(reasoner, data));
    }
}
