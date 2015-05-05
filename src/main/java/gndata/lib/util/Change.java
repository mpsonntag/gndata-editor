package gndata.lib.util;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Created by andrey on 30.04.15.
 */
public class Change {

    Model change;
    boolean positive;

    public Change(Model change, boolean positive) {
        this.change = change;
        this.positive = positive;
    }

    public boolean isPositive() {
        return positive;
    }

    public Model getChange() {
        return change;
    }
}
