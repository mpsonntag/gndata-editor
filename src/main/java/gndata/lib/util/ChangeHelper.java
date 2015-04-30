package gndata.lib.util;

import java.util.*;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.listeners.StatementListener;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.*;
import gndata.lib.util.change.*;

/**
 * A helper for changing RDF graph.
 */
public class ChangeHelper {

    private Model model;
    private OntModel ontology;

    private List<Change> changes;
    private int position;

    private boolean validate = false; // TODO do smth about it

    public ChangeHelper(Model m, OntModel ont) {
        this.model = m;
        this.ontology = ont;
        this.changes = new ArrayList<>();

        this.model.register(new ChangeListener());
        this.ontology.register(new ChangeListener());
    }

    /*   Public interface   */

    public void undo() {

        if (!(position > 0)) {
            throw new IllegalStateException("No previous changes recorded");
        }

        if (isLastUpdate()) {  // this is an update
            Change toRemove = changes.get(position);
            Change toAdd = changes.get(position - 1);

            model.remove(toRemove.getChange());
            position -= 1;

            model.add(toAdd.getChange());
            position -= 1;

        } else {  // these are non-update operations
            Change ch = changes.get(position);

            if (ch.isPositive()) {
                model.remove(ch.getChange());
            } else {
                model.add(ch.getChange());
            }

            position -= 1;
        }

        if (validate) {
            if (!validate().isValid()) {
                redo();
                throw new IllegalStateException("Undo leads to inconsistent state");
            }
        }

    }

    public void redo() {
        if (!(position < changes.size())) {
            throw new IllegalStateException("Nothing to re-do");
        }

        if (isNextUpdate()) {  // this is an update
            Change toRemove = changes.get(position + 1);
            Change toAdd = changes.get(position + 2);

            model.remove(toRemove.getChange());
            position += 1;

            model.add(toAdd.getChange());
            position += 1;

        } else {  // these are non-update operations
            position += 1;

            Change ch = changes.get(position);

            if (ch.isPositive()) {
                model.add(ch.getChange());
            } else {
                model.remove(ch.getChange());
            }
        }

        if (validate) {
            if (!validate().isValid()) {
                undo();
                throw new IllegalStateException("Redo leads to inconsistent state");
            }
        }
    }

    /*   Private helper functions   */


    private boolean isLastUpdate() {
        // position > 1;

        // TODO write code

        return false;
    }

    private boolean isNextUpdate() {
        // position < changes.size() - 1;

        // TODO write code

        return false;
    }

    private ValidityReport validate() {
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner.bindSchema(ontology);
        InfModel infm = ModelFactory.createInfModel(reasoner, model);

        return infm.validate();
    }

    private class ChangeListener implements SimpleModelChangeHandler {

        private void logChange(Change ch) {

            // do not log undo changes
            if (!isUndo(ch) && !isRedo(ch)) {

                // forget all undone changes, if any
                if (position != changes.size()) {
                    changes.subList(position, changes.size()).clear();
                }

                changes.add(ch);
                position = changes.size();
            }
        }

        private boolean isUndo(Change newCh) {
            Change oldCh = changes.get(position);

            boolean isEqual = oldCh.getChange().equals(newCh.getChange());
            boolean isOpposite = oldCh.isPositive() ^ newCh.isPositive();

            return isEqual && isOpposite;
        }

        private boolean isRedo(Change newCh) {
            Change oldCh = changes.get(position + 1);

            boolean isEqual = oldCh.getChange().equals(newCh.getChange());
            boolean isOpposite = oldCh.isPositive() == newCh.isPositive();

            return position < changes.size() && isEqual && isOpposite;
        }

        public void logAddChange(Model m) {
            logChange(new Change(m, true));
        }

        public void logRemoveChange(Model m) {
            logChange(new Change(m, false));
        }
    }


}
