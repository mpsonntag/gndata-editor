package gndata.app.ui.util;

import javafx.event.EventHandler;
import javafx.scene.input.*;

/**
* Created by msonntag on 04.12.14.
*/
public abstract class DoubleClickHandler implements EventHandler<MouseEvent> {

    private final static long DEFAULT_CLICK_INTERVAL = 500;

    private final long interval;
    private long lastClick;

    public DoubleClickHandler() {
        this(DEFAULT_CLICK_INTERVAL);
    }

    public DoubleClickHandler(long interval) {
        this.interval = interval;
        lastClick = 0;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            long diff = System.currentTimeMillis() - lastClick;
            if (diff < interval) {
                handleDoubleClick(mouseEvent);
                lastClick = 0;
            } else {
                lastClick = System.currentTimeMillis();
            }
        }
    }

    public abstract void handleDoubleClick(MouseEvent mouseEvent);
}
