package gndata.lib.util;

import java.util.Comparator;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Comparator for {@link Resource}
 */
public class ResourceComparator implements Comparator<Resource> {

    @Override
    public int compare(Resource a, Resource b) {
        return a.getURI().compareTo(b.getURI());
    }
}
