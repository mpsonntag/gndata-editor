package gndata.lib.util.change;

import java.util.*;
import java.util.stream.Collectors;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Class that knows how to delete Metadata entities.
 */
public class Delete implements Change {

    private String id;
    private boolean isRecursive = true;
    private boolean applied = false;

    private Model changes = ModelFactory.createDefaultModel();

    public Delete(String id, boolean isRecursive) {
        this.id = id;
        this.isRecursive = isRecursive;
    }

    private void deleteSingle(Resource res, Model from) {
        if (from.containsResource(res)) {
            List<Statement> lhs = from.listStatements(res, null, (RDFNode)null).toList();
            List<Statement> rhs = from.listStatements(null, null, res).toList();

            changes.add(lhs).add(rhs);

            from.remove(changes.listStatements());

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

    public void applyTo(Model m) {
        deleteSingle(m.getResource(id), m);
        applied = true;
    }

    public void undoFrom(Model m) {
        // TODO add logic
    }

    public boolean applied() {
        return applied;
    }
}
