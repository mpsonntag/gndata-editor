package gndata.lib.util;

import java.util.Comparator;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Comparator for statements.
 */
public class StatementComparator implements Comparator<Statement> {

    private static final ResourceComparator resourceCmp = new ResourceComparator();

    @Override
    public int compare(Statement a, Statement b) {
        int cmp = a.getSubject().getURI().compareTo(b.getSubject().getURI());

        if (cmp == 0)
            cmp = a.getPredicate().getURI().compareTo(b.getPredicate().getURI());

        if (cmp == 0) {
            RDFNode objA = a.getObject();
            RDFNode objB = b.getObject();

            if (objA.isLiteral() && ! objB.isLiteral()) {
                cmp = 1;
            } else if (! objA.isLiteral() && objB.isLiteral()) {
                cmp = -1;
            } else if (objA.isLiteral() && objB.isLiteral()) {
                cmp = objA.toString().compareTo(objB.toString());
            } else {
                cmp = resourceCmp.compare(objA.asResource(), objB.asResource());
            }
        }

        return cmp;
    }
}
