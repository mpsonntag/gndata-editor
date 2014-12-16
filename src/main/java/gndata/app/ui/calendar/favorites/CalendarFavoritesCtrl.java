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
import org.controlsfx.control.CheckListView;

/**
 * Controller for the list of favorites to display.
 */
public class CalendarFavoritesCtrl implements Initializable {

    @FXML
    private CheckListView<ResourceAdapter> favoritesList;

    private final ProjectState ps;
    private final CalendarState cs;

    private ObservableList<ResourceAdapter> items;

    @Inject
    public CalendarFavoritesCtrl(ProjectState ps, CalendarState cs) {
        this.ps = ps;
        this.cs = cs;

        items = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateFavorites();

        favoritesList.setItems(items);
        favoritesList.getCheckModel().checkAll();

        favoritesList.getCheckModel().getCheckedItems().addListener(
                (ListChangeListener.Change<? extends ResourceAdapter> l) ->
                        cs.getSelectedTypes().setAll(favoritesList.getCheckModel().getCheckedItems())
        );

        ps.configProperty().addListener((p, o, n) -> {
            updateFavorites();
            favoritesList.getCheckModel().checkAll();
        });
    }

    private void updateFavorites() {
        if (ps.getMetadata() != null) {
            items.setAll(ps.getMetadata().getAvailableTypes()
                    .stream()
                    .map(r -> new ResourceAdapter(r, null))
                    .collect(Collectors.toList()));
        }
    }
}
