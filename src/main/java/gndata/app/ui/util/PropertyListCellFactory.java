package gndata.app.ui.util;

import javafx.scene.control.*;
import javafx.util.Callback;

import com.hp.hpl.jena.rdf.model.Property;

/**
 * Created by msonntag on 23.04.15.
 */
public class PropertyListCellFactory implements Callback<ListView<Property>, ListCell<Property>> {

    public PropertyListCellFactory() {}

    @Override
    public ListCell<Property> call(ListView<Property> p) {
        return new ListCell<Property>() {
            @Override
            protected void updateItem(Property item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setText(item.getLocalName());
                }
            }
        };
    }
}
