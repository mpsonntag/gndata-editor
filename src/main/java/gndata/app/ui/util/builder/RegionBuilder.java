package gndata.app.ui.util.builder;

import javafx.scene.layout.Region;

/**
 * Created by stoewer on 02.04.15.
 */
public abstract class RegionBuilder<T extends Region> extends NodeBuilder<T> {


    private double maxWidth = Region.USE_COMPUTED_SIZE;
    private double minWidth = Region.USE_COMPUTED_SIZE;
    private double prefWidth = Region.USE_COMPUTED_SIZE;

    private double maxHeight = Region.USE_COMPUTED_SIZE;
    private double minHeight = Region.USE_COMPUTED_SIZE;
    private double prefHeight = Region.USE_COMPUTED_SIZE;

    @Override
    protected T configure(T element) {
        element = super.configure(element);

        element.setMaxWidth(getMaxWidth());
        element.setMinWidth(getMinWidth());
        element.setPrefWidth(getPrefWidth());

        element.setMaxHeight(getMaxHeight());
        element.setMinHeight(getMinHeight());
        element.setPrefHeight(getPrefHeight());

        return element;
    }

    // getter and setter

    public double getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(double maxWidth) {
        this.maxWidth = maxWidth;
    }

    public double getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(double minWidth) {
        this.minWidth = minWidth;
    }

    public double getPrefWidth() {
        return prefWidth;
    }

    public void setPrefWidth(double prefWidth) {
        this.prefWidth = prefWidth;
    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }

    public double getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(double minHeight) {
        this.minHeight = minHeight;
    }

    public double getPrefHeight() {
        return prefHeight;
    }

    public void setPrefHeight(double prefHeight) {
        this.prefHeight = prefHeight;
    }
}
