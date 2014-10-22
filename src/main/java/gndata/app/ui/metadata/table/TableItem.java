package gndata.app.ui.metadata.table;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.rdf.model.*;


/**
 * Class that implements rendering of table items with RDF literals.
 */
public class TableItem {

    private final Property predicate;
    private final Literal literal;

    public TableItem(Property predicate, Literal literal) {
        this.predicate = predicate;
        this.literal = literal;
    }

    public String getPredicate() {
        return predicate.getLocalName();
    }

    public String getLiteral() {
        return literal.getValue().toString();
    }

    public String getType() {
        RDFDatatype dt = literal.getDatatype();

        return dt != null ? dt.getJavaClass().getSimpleName() : "";
    }
}