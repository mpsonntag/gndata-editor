package gndata.lib.util;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.*;
import gndata.lib.util.change.*;

/**
 * A helper for changing RDF graph.
 */
public class ChangeHelper {

    private Model model;
    private OntModel ontology;
    private Tracker tracker;
    private Reasoner reasoner;

    public ChangeHelper(Model m, OntModel ont) {
        this.model = m;
        this.ontology = ont;
        this.tracker = new Tracker();

        this.reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner.bindSchema(ontology);
    }

    private void applyChange(Change ch) {
        ch.applyTo(model, ontology);

        InfModel infm = ModelFactory.createInfModel(reasoner, model);

        if (infm.validate().isValid()) {
            tracker.logChange(ch);
        } else {
            ch.undoFrom(model);
        }
    }

    /*   Public interface   */

    public void create(Model NewObject) {
        Change ch = new Insert(NewObject);
        applyChange(ch);
    }

    public void update(Model newObject) {
        Change ch = new Insert(newObject);
        applyChange(ch);
    }

    public void delete(String uri) {
        Change ch = new Delete(uri);
        applyChange(ch);
    }

    // TODO add more methods
}
