package gndata.app.ui.util;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import org.controlsfx.control.PopOver;

/**
 * This class provides ControlFX PopOver Nodes
 */
public class CreatePopOver {

    public static PopOver createPopOver(String msg) {
        PopOver pov = new PopOver();
        pov.setAutoFix(true);
        pov.setHideOnEscape(true);
        pov.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> pov.hide());
        pov.setContentNode(new Label(msg));

        return pov;
    }

    public static PopOver createPopOver(String msg, Node n) {
        PopOver pov = createPopOver(msg);
        pov.show(n);

        return pov;
    }

    public static PopOver createPopOver(String msg, Node n, double offset) {
        PopOver pov = createPopOver(msg);
        pov.show(n, offset);

        return pov;
    }
}