package gndata.lib.util.change;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * Class that knows how to insert new Metadata entities.
 */
public class Insert extends AbstractChange {

    private Model newObject;

    public Insert(Model newObject) {
        this.newObject = newObject;
    }

    public void applyTo(Model m, OntModel o) throws IllegalStateException {
        if (hasChanges()) {
            throw new IllegalStateException("Changes already applied");
        }

        if (!newObject.listStatements().hasNext()) {
            throw new IllegalStateException("No changes to apply");
        }

        m.add(newObject);
        addInserted(newObject);
    }
}
