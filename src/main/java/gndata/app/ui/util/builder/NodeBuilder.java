package gndata.app.ui.util.builder;

import javafx.scene.Node;
import javafx.util.Builder;

/**
 * Created by stoewer on 02.04.15.
 */
public abstract class NodeBuilder<T extends Node> implements Builder<T> {

    private String style;

    protected T configure(T element) {
        element.setStyle(getStyle());

        return element;
    }

    // getter and setter

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
