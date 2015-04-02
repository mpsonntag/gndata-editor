package gndata.lib.util.change;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Interface for changes in the RDF graph.
 */
public interface Change {

    public void applyTo(Model m);

    public void undoFrom(Model m);
}
