package gndata.app.ui.util.converter;

import javafx.util.StringConverter;

import com.hp.hpl.jena.rdf.model.Property;

/**
 * class handling which text is displayed when an item from the
 * add new DataProperty combo box has been selected
 */
final public class PropertyStringConverter extends StringConverter<Property> {

    public PropertyStringConverter() {}

    @Override
    public String toString(Property prop) {
        return prop != null ? prop.getLocalName() : null;
    }

    @Override
    public Property fromString(String str) {
        return null;
    }
}