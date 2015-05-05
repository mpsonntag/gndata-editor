package gndata.lib.util;

import java.util.*;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.*;

/**
 * A helper for changing RDF graph.
 */
public class ChangeHelper {

    private Model model;
    private OntModel ontology;

    private List<Change> changes;
    private int position = -1;

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

        if (!(position > -1)) {
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
        if (!(position < changes.size() - 1)) {
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
            Change ch = changes.get(position + 1);

            if (ch.isPositive()) {
                model.add(ch.getChange());
            } else {
                model.remove(ch.getChange());
            }

            position += 1;
        }

        if (validate) {
            if (!validate().isValid()) {
                undo();
                throw new IllegalStateException("Redo leads to inconsistent state");
            }
        }
    }

    public Change get(int index) {
        return changes.get(index);
    }

    public int size() {
        return changes.size();
    }

    /*   Private helper functions   */

    private static boolean isUpdate(Change removed, Change added) {

        // assume update is always first remove and then add

        if (added.getChange().size() != removed.getChange().size()) {
            return false;
        }

        StmtIterator iter = removed.getChange().listStatements();
        while (iter.hasNext()) {
            Statement st = iter.nextStatement();

            if (added.getChange()
                    .listStatements(st.getSubject(), st.getPredicate(), (RDFNode) null)
                    .toList().size() != 1 ) {
                return false;
            }
        }

        return true;
    }

    private boolean isLastUpdate() {
        return position > 0 && isUpdate(changes.get(position - 1), changes.get(position));
    }

    private boolean isNextUpdate() {
        boolean posCheck = position < changes.size() - 1 && position + 2 < changes.size();
        return posCheck && isUpdate(changes.get(position + 1), changes.get(position + 2));
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
                if (position < changes.size() - 1) {
                    changes.subList(position, changes.size()).clear();
                }

                changes.add(ch);
                position = changes.size() - 1;
            }
        }

        private boolean isUndo(Change newCh) {
            if (changes.size() > 0 && position > -1) {
                Change oldCh = changes.get(position);

                boolean isEqual = oldCh.getChange().difference(newCh.getChange()).isEmpty();
                boolean isOpposite = oldCh.isPositive() ^ newCh.isPositive();

                return isEqual && isOpposite;
            } else {
                return false;
            }
        }

        private boolean isRedo(Change newCh) {
            if (position < changes.size() - 1) {
                Change oldCh = changes.get(position + 1);

                boolean isEqual = oldCh.getChange().difference(newCh.getChange()).isEmpty();
                boolean isOpposite = oldCh.isPositive() == newCh.isPositive();

                return isEqual && isOpposite;
            } else {
                return false;
            }
        }

        public void logAddChange(Model m) {
            logChange(new Change(m, true));
        }

        public void logRemoveChange(Model m) {
            logChange(new Change(m, false));
        }
    }


}
