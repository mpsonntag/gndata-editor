package gndata.lib.util.change;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;


/**
 * Common methods for all types of RDF graph changes.
 */
public abstract class AbstractChange implements OldChange {

    private Model inserted = ModelFactory.createDefaultModel();
    private Model removed = ModelFactory.createDefaultModel();

    public abstract void applyTo(Model m, OntModel o) throws IllegalStateException;

    public void undoFrom(Model m) throws IllegalStateException {
        if (!hasChanges()) {
            throw new IllegalStateException("Operation has no changes, can't undo");
        }

        m.remove(inserted).add(removed);
    }

    public boolean hasChanges() {
        return inserted.listStatements().hasNext() || removed.listStatements().hasNext();
    }

    protected void addInserted(Model m) {
        inserted.add(m);
    }

    protected void addRemoved(Model m) {
        removed.add(m);
    }
}
