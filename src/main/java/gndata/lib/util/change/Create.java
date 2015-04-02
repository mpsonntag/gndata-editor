package gndata.lib.util.change;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;

/**
 * Class that knows how to create new Metadata entities.
 */
public class Create implements Change {

    private Model new_object;
    private OntModel ontology;
    private boolean applied = false;

    private Model changes = ModelFactory.createDefaultModel();

    public Create(Model new_object, OntModel ontology) {
        this.new_object = new_object;
        this.ontology = ontology;
    }

    public void applyTo(Model m) {
        // TODO add logic

        // model should have an RDF.type from related ontology
        RDFNode obj_type_l = new_object.listObjectsOfProperty(RDF.type).toList().get(0);
        Resource obj_type_r = ontology.getResource(obj_type_l.toString());

        if (ontology.contains(obj_type_r, RDFS.subClassOf)) {
            if (!m.containsAny(new_object)) {

                // record changes
                Model tmp = ModelFactory.createDefaultModel();
                tmp.add(m.intersection(new_object));
                tmp.add(new_object);
                changes = tmp;

                m.add(new_object);
                applied = true;
            }
        }
    }

    public void undoFrom(Model m) {
        // TODO add logic
    }

    public boolean applied() {
        return applied;
    }
}
