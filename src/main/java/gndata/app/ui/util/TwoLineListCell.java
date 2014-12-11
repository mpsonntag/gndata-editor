package gndata.app.ui.util;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Two line list cell with properties for lines text and icon.
 */
public abstract class TwoLineListCell<T> extends ListCell<T> {

    protected final StringProperty lineOne, lineTwo;
    protected final ObjectProperty<Image> icon;

    public TwoLineListCell() {
        Label labelOne = new Label();
        lineOne = labelOne.textProperty();
        labelOne.setStyle("-fx-font-weight: bold");

        Label labelTwo = new Label();
        lineTwo = labelTwo.textProperty();
        VBox vbox = new VBox(labelOne, labelTwo);
        labelTwo.setStyle("-fx-text-fill: #666666");

        ImageView iconView = new ImageView();
        icon = iconView.imageProperty();

        HBox hbox = new HBox(iconView, vbox);
        hbox.setStyle("-fx-spacing: 10");

        setGraphic(hbox);
    }

    /**
     * Use this method to set the values of {@link #lineOne}, {@link #lineTwo} and
     * {@link #icon}.
     *
     * @param item  The item to get the text and icon information from.
     * @param empty Whether the cell is empty or not.
     */
    protected abstract void update(T item, boolean empty);

    @Override
    protected final void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        update(item, empty);
    }
}
