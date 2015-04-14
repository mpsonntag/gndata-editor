package gndata.lib.util.change;

import java.util.*;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * This class records changes and provides interface to access them.
 */
public class Tracker {

    private List<Change> changes;
    private int position;

    public Tracker() {
        changes = new ArrayList<Change>();
    }

    public List<Change> getChanges() {
        return changes;
    }

    public void logChange(Change ch) {
        changes.add(ch);
        position = changes.size();
    }

    public void undo(Model m) {
        changes.get(position).undoFrom(m);
        position -= 1;
    }

    public void redo(Model m, OntModel o) {
        if (position < changes.size() - 1) {
            changes.get(position).applyTo(m, o);
            position += 1;
        }
    }
}
