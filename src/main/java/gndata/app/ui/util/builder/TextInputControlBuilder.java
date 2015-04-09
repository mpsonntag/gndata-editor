package gndata.app.ui.util.builder;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TextInputControl;

/**
 * Created by stoewer on 02.04.15.
 */
public abstract class TextInputControlBuilder<T extends TextInputControl> extends RegionBuilder<T> {

    public boolean editable = true;
    public String promptText = "";
    public StringProperty text;

    @Override
    protected T configure(T element) {
        element = super.configure(element);

        element.setEditable(isEditable());
        element.setPromptText(getPromptText());
        if (text != null)
            element.textProperty().bindBidirectional(text);

        return element;
    }

    // getter and setter

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getPromptText() {
        return promptText;
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    public StringProperty getText() {
        return text;
    }

    public void setText(StringProperty text) {
        this.text = text;
    }
}
