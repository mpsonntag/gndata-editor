package gndata.lib.util;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;
import gndata.lib.util.change.*;

/**
 * A helper for changing RDF graph.
 */
public class ChangeHelper {

    private Model model;
    private OntModel ontology;
    private Tracker tracker;

    public ChangeHelper(Model m, OntModel ont) {
        this.model = m;
        this.ontology = ont;
        this.tracker = new Tracker();
    }

    private void applyChange(Change ch) {
        ch.applyTo(model);

        tracker.logChange(ch);
    }

    /*   Public interface   */

    public void create(Model new_object) {
        Change ch = new Merge(new_object, ontology);
        applyChange(ch);
    }

    public void delete(Resource res) {
        Change ch = new Delete(res.getURI(), true);
        applyChange(ch);
    }

    // TODO add more methods
}
