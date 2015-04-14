package gndata.lib.util.change;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;

/**
 * Class that knows how to delete Metadata entities.
 */
public class Delete extends AbstractChange {

    private String uri;

    public Delete(String uri) {
        this.uri = uri;
    }

    private void deleteSingle(Resource res, Model from) {
        if (from.containsResource(res)) {
            Model toRemove = ModelFactory.createDefaultModel();
            toRemove.add(from.listStatements(res, null, (RDFNode) null));
            toRemove.add(from.listStatements(null, null, res));

            from.remove(toRemove);
            addRemoved(toRemove);

            /*
            // delete all res-to-object entities that have no other references
            // is subject for further discussion

            if (isRecursive) {
                related.forEach(r -> {
                    if (from.listSubjectsWithProperty(null, r.getURI())
                            .toList().isEmpty()) {
                        deleteSingle(r, from);
                    }
                });
            }
             */
        }

    }

    public void applyTo(Model m, OntModel o) throws IllegalStateException {
        if (hasChanges()) {
            throw new IllegalStateException("Changes already applied");
        }

        deleteSingle(m.getResource(uri), m);
    }
}
