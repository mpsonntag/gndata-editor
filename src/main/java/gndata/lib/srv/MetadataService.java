package gndata.lib.srv;

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

    private OntModel schema;    // union model for all imported ontology files
    private Model annotations;  // model for data annotations

    public MetadataService(OntModel schema, Model annotations) {
        this.schema = schema;
        this.annotations = annotations;
    }

    /**
     * Returns a Ontology RDF Model instance to access default and
     * custom ontology terms.
     *
     * @return  Ontology Model
     */
    public OntModel getSchema() {
        return schema;
    }

    /**
     * Returns an RDF Model instance to access annotations as stored RDF triples.
     *
     * @return  Ontology Model
     */
    public Model getAnnotations() {
        return annotations;
    }

    /**
     * Creates a new model with inferred relations based on loaded ontology,
     * annotations and reasoner.
     *
     * @return  Model with inferred relations
     */
    public InfModel getAnnotationsWithInference() {
        return ModelFactory.createInfModel(getReasoner(), annotations);
    }

    /**
     * Creates a new reasoner based on actual schema and annotations.
     *
     * @return  Reasoner
     */
    public Reasoner getReasoner() {
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        return reasoner.bindSchema(schema);
    }

    /**
     * Creates a new Metadata Service using a given path. Combines existing
     * project RDF schemas (ontology files) and metadata storage (annotations)
     * into a common Model. Creates default schemas if some do not exist.
     *
     * @return          MetadataService
     */
    public static MetadataService create(String projectPath) throws IOException {
        if (projectPath == null) {
            throw new IOException("Cannot create metadata service at non-existing path");
        }

        MetadataFilesManager metaFiles = new MetadataFilesManager(projectPath);

        OntModel schema = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM);

        // TODO figure out if using OntDocumentManager to read files makes sense
        for (Path p : metaFiles.schemaPaths()) {
            schema.addSubModel(RDFDataMgr.loadModel(p.toString()));
        }

        Model data = RDFDataMgr.loadModel(metaFiles.annotationsPath().toString());

        return new MetadataService(schema, data);
    }
}
