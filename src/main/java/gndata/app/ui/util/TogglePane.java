package gndata.app.ui.util;

import javafx.beans.property.*;
import javafx.scene.Node;
import javafx.scene.layout.*;

/**
 * Panel for switching between to other panels.
 *
 * The panel provides two alternative child nodes. Depending on the state of
 * {@link #showFirst} either the first child is shown or the second one.
 */
public class TogglePane extends Region {

    private final ObjectProperty<Node> first, second;
    private final BooleanProperty showFirst;


    public TogglePane() {
        super();

        first = new SimpleObjectProperty<>();
        second = new SimpleObjectProperty<>();
        showFirst = new SimpleBooleanProperty(true);

        first.addListener((p, o, n) -> {
            if (showFirst.get()) {
                getChildren().setAll(n);
            }
        });

        second.addListener((p, o, n) -> {
            if (! showFirst.get()) {
                getChildren().setAll(n);
            }
        });

        showFirst.addListener((p, o, n) -> {
            if (! n) {
                getChildren().setAll(second.get());
            } else {
                getChildren().setAll(first.get());
            }
        });
    }

    /**
     * The node to show when showFirst is true.
     *
     * @return The first node.
     */
    public Node getFirst() {
        return first.get();
    }

    public ObjectProperty<Node> firstProperty() {
        return first;
    }

    public void setFirst(Node first) {
        this.first.set(first);
    }

    /**
     * The node to show when the showFirst is false.
     *
     * @return The second node.
     */
    public Node getSecond() {
        return second.get();
    }

    public ObjectProperty<Node> secondProperty() {
        return second;
    }

    public void setSecond(Node second) {
        this.second.set(second);
    }

    /**
     * State to switch between panels.
     *
     * @return True if showFirst is true, false otherwise.
     */
    public boolean getShowFirst() {
        return showFirst.get();
    }

    public BooleanProperty showFirstProperty() {
        return showFirst;
    }

    public void setShowFirst(boolean showFirst) {
        this.showFirst.set(showFirst);
    }

}
