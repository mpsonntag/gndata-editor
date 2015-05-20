package gndata.app.ui.util;

import java.util.*;
import java.util.function.Function;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.scene.control.*;

import static java.util.stream.Collectors.toList;

import gndata.lib.srv.*;

import javafx.scene.layout.Region;
import org.controlsfx.control.SegmentedButton;

/**
 * Component that represents a bread crumb navigation bar.
 *
 * The elements of in the navigation bar are represented as a list of FileAdapter
 * instances and each instance is used to create a button in the navigation bar.
 */
public class BreadCrumbNav<T extends IFileAdapter> extends Region {

    private ObjectProperty<ObservableList<T>> items;
    private SelectionModel<T> selectionModel;
    private Function<T, String> labelFactory;

    private ItemListListener itemListListener;

    private SegmentedButton buttons;

    /**
     * Default constructor.
     */
    public BreadCrumbNav() {
        this(FXCollections.observableList(new LinkedList<T>()));
    }

    /**
     * Constructor that initializes the bar with an observable list.
     *
     * @param items The items in the navigation bar.
     */
    public BreadCrumbNav(ObservableList<T> items) {
        super();

        this.items = new SimpleObjectProperty<>();
        this.items.addListener(new ItemsListener());

        this.selectionModel = new NavSelectionModel();
        this.selectionModel.selectedItemProperty().addListener(new SelectionListener());

        this.buttons = new SegmentedButton();
        this.buttons.getToggleGroup().selectedToggleProperty().addListener(new ToggleListener());
        getChildren().add(this.buttons);

        this.itemListListener = new ItemListListener();
        this.labelFactory = T::getFileName;

        this.items.set(items);
    }

    /**
     * The list of items in the navigation bar.
     *
     * @return List of navigation items.
     */
    public ObservableList<T> getItems() {
        return items.get();
    }

    public ObjectProperty<ObservableList<T>> itemsProperty() {
        return items;
    }

    public void setItems(ObservableList<T> items) {
        this.items.set(null);
        this.items.set(items);
    }

    /**
     * The selection model of the navigation bar.
     *
     * @return The selection model.
     */
    public SelectionModel<T> getSelectionModel() {
        return selectionModel;
    }

    /**
     * Factory used for the creation of button labels. By default a factory is
     * created that uses {@link Object::toString}.
     *
     * @return The label creation factory.
     */
    public Function<T, String> getLabelFactory() {
        return labelFactory;
    }

    public void setLabelFactory(Function<T, String> labelFactory) {
        this.labelFactory = labelFactory;
    }


    /**
     * Update the buttons according to the content of the items list.
     * For internal use only.
     */
    private void updateButtons() {
        buttons.getButtons().setAll(
                items.get().stream()
                        .map(fa -> new ToggleButton(labelFactory.apply(fa)))
                        .collect(toList())
        );
    }

    /**
     * Set the selected item. For internal use only.
     *
     * @param selected The new item to select, if null the default (last)
     *                 will be selected.
     */
    private void select(T selected) {
        List<T> l = items.get();

        if (l == null || l.isEmpty())
            return;

        if (selected == null)
            selected = l.get(l.size() - 1);

        selectionModel.select(selected);
    }

    /**
     * Select the default item (the last one in the item list).
     * For internal use only.
     */
    private void selectDefault() {
        select(null);
    }

    /**
     * Listener that reacts on replacement of the whole items list.
     */
    private class ItemsListener implements ChangeListener<ObservableList<T>> {

        @Override
        public void changed(ObservableValue<? extends ObservableList<T>> observable, ObservableList<T> oldValue, ObservableList<T> newValue) {
            if (oldValue != null) {
                oldValue.removeListener(itemListListener);
            }

            if (newValue != null) {
                updateButtons();
                selectDefault();

                newValue.addListener(itemListListener);
            }
        }
    }

    /**
     * Listener on changes of the elements in the items list.
     */
    private class ItemListListener implements ListChangeListener<T> {

        @Override
        public void onChanged(Change<? extends T> change) {
            updateButtons();
            selectDefault();
        }
    }

    /**
     * Listener for changes of the toggle state of the buttons.
     */
    private class ToggleListener implements ChangeListener<Toggle> {

        @Override
        public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
            ToggleButton btn = (ToggleButton) newValue;
            int position = buttons.getButtons().indexOf(btn);
            select(items.get().get(position));
        }
    }

    /**
     * Listen on changes of the selection model.
     */
    private class SelectionListener implements ChangeListener<T> {

        @Override
        public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {

            List<T> l = new ArrayList<>(items.get());

            int pos = l.indexOf(newValue);

            if (pos < 0) {

                for (pos = l.size() - 1; pos >= 0; pos--) {

                    T curr = l.get(pos);
                    if (curr.hasChild(newValue))
                        break;

                    l.remove(pos);
                }

                l.add(newValue);
                items.get().setAll(l);
                pos++;
            }

            ToggleButton toggle = buttons.getButtons().get(pos);
            buttons.getToggleGroup().selectToggle(toggle);
        }

    }

    /**
     * Selection model for the navigation bar.
     */
    private class NavSelectionModel extends SingleSelectionModel<T> {

        @Override
        protected T getModelItem(int index) {
            return items.get().get(index);
        }

        @Override
        protected int getItemCount() {
            return items.get().size();
        }
    }
}
