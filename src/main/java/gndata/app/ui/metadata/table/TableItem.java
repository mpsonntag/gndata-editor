package gndata.app.ui.metadata.table;

import javafx.beans.property.SimpleStringProperty;


/**
 * Class that implements rendering of table items.
 */
public class TableItem {

    private final SimpleStringProperty predicate;
    private final SimpleStringProperty literal;
    private final SimpleStringProperty type;

    public TableItem(String predicate, String literal, String type) {
        this.predicate = new SimpleStringProperty(predicate);
        this.literal = new SimpleStringProperty(literal);
        this.type = new SimpleStringProperty(type);
    }

    public String getPredicate() {
        return predicate.get();
    }

    public SimpleStringProperty predicateProperty() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate.set(predicate);
    }

    public String getLiteral() {
        return literal.get();
    }

    public SimpleStringProperty literalProperty() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal.set(literal);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }
}