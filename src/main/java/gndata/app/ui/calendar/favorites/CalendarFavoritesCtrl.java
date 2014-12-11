package gndata.app.ui.calendar.favorites;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.Callback;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;
import gndata.app.state.*;
import gndata.lib.srv.ResourceAdapter;

/**
 * Controller for the list of favorites to display.
 */
public class CalendarFavoritesCtrl implements Initializable {

    @FXML
    private ListView<SelectableResource> favoritesList;

    private final ProjectState ps;
    private final CalendarState cs;

    @Inject
    public CalendarFavoritesCtrl(ProjectState ps, CalendarState cs) {
        this.ps = ps;
        this.cs = cs;

        this.ps.configProperty().addListener((p, o, n) -> updateFavorites());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Callback<ListView<SelectableResource>, ListCell<SelectableResource>> forListView =
                CheckBoxListCell.forListView(SelectableResource::selectedProperty);
        favoritesList.setCellFactory(forListView);

        updateFavorites();
    }

    private void updateFavorites() {
        ObservableList<SelectableResource> lst = FXCollections.observableArrayList();

        if (ps.getMetadata() != null) {
            lst.addAll(ps.getMetadata().getAvailableTypes()
                    .stream()
                    .map(r -> new SelectableResource(r, null))
                    .collect(Collectors.toList()));
        }

        // setup listeners for all checkboxes
        lst.forEach(sr -> sr.selectedProperty().addListener(
                (p, o, n) -> cs.getSelectedTypes().setAll(
                        favoritesList.getItems()
                                .filtered(SelectableResource::getSelected)))
        );

        favoritesList.setItems(lst);
    }

    /**
     * A helper class to make ResourceAdapter objects selectable in the list.
     */
    class SelectableResource extends ResourceAdapter {

        private final SimpleBooleanProperty selected;

        public SelectableResource(Resource resource, ResourceAdapter parent) {
            super(resource, parent);

            this.selected = new SimpleBooleanProperty(true);
        }

        public boolean getSelected() {
            return selected.get();
        }

        public void setSelected(boolean selected) {
            this.selected.set(selected);
        }

        public SimpleBooleanProperty selectedProperty() {
            return selected;
        }
    }
}
