package gndata.lib.util;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Created by andrey on 30.04.15.
 */
public class Change {

    Model change;
    boolean isAddition;

    public Change(Model change, boolean isAddition) {
        this.change = change;
        this.isAddition = isAddition;
    }

    public boolean isAddition() {
        return isAddition;
    }

    public Model getChange() {
        return change;
    }
}
