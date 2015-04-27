package gndata.app.ui.util.filter;

import java.util.function.UnaryOperator;
import javafx.beans.property.*;
import javafx.scene.control.TextFormatter.Change;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;

/**
 * Filter input dependent on provided XSDDatatype
 */
public final class LiteralFilter implements UnaryOperator<Change> {

    public final ObjectProperty<XSDDatatype> type;

    public LiteralFilter() {
        this((XSDDatatype) null);
    }

    public LiteralFilter(ObjectProperty<XSDDatatype> type) { this.type = type; }

    public LiteralFilter(XSDDatatype type) { this.type = new SimpleObjectProperty<>(type); }

    public XSDDatatype getType() {
        return type.get();
    }

    public ObjectProperty<XSDDatatype> typeProperty() {
        return type;
    }

    public void setType(XSDDatatype type) {
        this.type.set(type);
    }

    @Override
    public Change apply(Change change) {
        // TODO does not accept "-" as the first letter of a number
        // "-" can be entered, if there are already numbers in the textfield
        if (change.isContentChange() && type.isNotNull().get() && !change.getControlNewText().isEmpty()) {
            System.out.println(type.get().getURI()+" "+change.getControlNewText());

            if (!type.get().isValid(change.getControlNewText())) {
                return null;
            }
        }
        return change;
    }
}
