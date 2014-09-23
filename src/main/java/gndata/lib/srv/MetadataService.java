package gndata.lib.srv;

import com.hp.hpl.jena.rdf.model.*;
import gndata.lib.config.ProjectConfig;
import org.apache.jena.riot.RDFDataMgr;

/**
 * Class implementing main functions working with project metadata
 */
public class MetadataService {

    private ProjectConfig config;
    private Model model;

    public MetadataService(ProjectConfig config) {
        this.config = config;
    }

    /**
     * Returns a common RDF Model instance to access all metadata triples
     * and ontology terms.
     *
     * @return  RDF Model
     */
    public Model getModel() {
        if (model == null) {
            Model customOWL = RDFDataMgr.loadModel(ProjectConfig.CUSTOM_ONT_PATH.toString());
            Model defaultOWL = RDFDataMgr.loadModel(ProjectConfig.ONTOLOGY_PATH.toString());
            Model primaryRDF = RDFDataMgr.loadModel(ProjectConfig.METADATA_PATH.toString());

            // TODO investigate a way to manage models separately

            model = defaultOWL.union(customOWL).union(primaryRDF);
        }

        return model;
    }

    public void addCustomProperty(String name) {
        // TODO implement
    }

    public void addCustomTerm(Resource s, Property p, RDFNode o) {
        // TODO implement
    }

    public static MetadataService create(ProjectConfig config) {
        return new MetadataService(config);
    }

}
