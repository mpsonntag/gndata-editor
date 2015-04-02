package gndata.lib.util.change;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Class that knows how to delete Metadata entities.
 */
public class Delete implements Change {

    private Resource res;
    private boolean applied = false;

    public Delete(Resource res) {
        this.res = res;
    }

    public void applyTo(Model m) {
        // TODO add logic

        if (!m.containsResource(res)) {
            // delete all literals

            // delete all subject-to-res relations

            // delete all res-to-object entities that have no other references
        }
    }

    public void undoFrom(Model m) {
        // TODO add logic
    }

    public boolean applied() {
        return applied;
    }
}
