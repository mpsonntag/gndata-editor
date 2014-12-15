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

    @Inject
    public CalendarFavoritesCtrl(ProjectState ps, CalendarState cs) {
        this.ps = ps;
        this.cs = cs;

        this.ps.configProperty().addListener((p, o, n) -> updateFavorites());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateFavorites();
    }

    private void updateFavorites() {
        ObservableList<ResourceAdapter> lst = FXCollections.observableArrayList();

        if (ps.getMetadata() != null) {
            lst.addAll(ps.getMetadata().getAvailableTypes()
                    .stream()
                    .map(r -> new ResourceAdapter(r, null))
                    .collect(Collectors.toList()));
        }

        favoritesList.setItems(lst);
        favoritesList.getCheckModel().checkAll();

        favoritesList.getCheckModel().getCheckedItems().addListener(
                (ListChangeListener.Change<? extends ResourceAdapter> l) ->
                    cs.getSelectedTypes().setAll(favoritesList.getCheckModel().getCheckedItems())
        );
    }
}
