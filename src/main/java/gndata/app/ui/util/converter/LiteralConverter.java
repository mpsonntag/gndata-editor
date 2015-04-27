package gndata.app.ui.util.converter;

import javafx.beans.property.*;
import javafx.util.StringConverter;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.*;

/**
 * Created by msonntag on 24.04.15.
 */
public final class LiteralConverter extends StringConverter<Literal> {

    public final ObjectProperty<XSDDatatype> type;

    public LiteralConverter() {
        this((XSDDatatype) null);
    }

    public LiteralConverter(ObjectProperty<XSDDatatype> type) {
        this.type = type;
    }

    public LiteralConverter(XSDDatatype type) {
        this.type = new SimpleObjectProperty<>(type);
    }

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
    public String toString(Literal object) {
        return object == null ? "" : object.getLexicalForm();
    }

    @Override
    public Literal fromString(String string) {
        Literal l;
        if (type.isNull().get()) {
            l = ResourceFactory.createPlainLiteral(string);
        } else {
            l = ResourceFactory.createTypedLiteral(string, type.get());
        }
        return l;
    }
}
