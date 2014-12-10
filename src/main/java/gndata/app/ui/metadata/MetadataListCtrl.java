package gndata.app.ui.metadata;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import com.google.inject.Inject;
import gndata.app.state.MetadataNavState;
import gndata.app.ui.util.DoubleClickHandler;
import gndata.lib.srv.ResourceAdapter;

/**
 * Controller for the metadata list.
 */
public class MetadataListCtrl implements Initializable {


    @FXML
    private ListView<ResourceAdapter> metadataListView;
    @FXML
    private TextField filterTextField;

    private final MetadataNavState navState;
    private final StringProperty filter;
    private final ObservableList<ResourceAdapter> filteredList;
    private final List<ResourceAdapter> unfilteredList;

    @Inject
    public MetadataListCtrl(MetadataNavState navState) {
        this.navState = navState;

        filter = new SimpleStringProperty();
        filteredList = FXCollections.observableList(new ArrayList<ResourceAdapter>());
        unfilteredList = new ArrayList<>();

        this.navState.selectedParentProperty().addListener(new SelectedParentListener());
        filter.addListener((p, o, n) -> applyFilter(n));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filterTextField.textProperty().bindBidirectional(filter);

        metadataListView.setItems(filteredList);
        navState.selectedNodeProperty().bind(metadataListView.getSelectionModel().selectedItemProperty());

        metadataListView.addEventHandler(MouseEvent.MOUSE_CLICKED, new ListNavigationHandler());
    }


    public void applyFilter(String filter) {

        if (filter == null || filter.equals("")) {
            filteredList.setAll(unfilteredList);
        } else {
            filteredList.setAll(
                    unfilteredList.stream()
                            .filter(ra -> ra.getFileName().contains(filter))
                            .collect(Collectors.toList())
            );
        }
    }


    private class SelectedParentListener implements ChangeListener<ResourceAdapter> {

        @Override
        public void changed(ObservableValue<? extends ResourceAdapter> observable, ResourceAdapter oldValue,
                            ResourceAdapter newValue) {

            unfilteredList.clear();
            unfilteredList.addAll(newValue.getChildren());

            String fltr = filter.get();
            if (fltr == null || fltr.equals("")) {
                applyFilter(null); // just filter with null
            } else {
                filter.set(null); // reset the filter (triggers filtering)
            }

        }
    }


    private class ListNavigationHandler extends DoubleClickHandler {

        @Override
        public void handleDoubleClick(MouseEvent mouseEvent) {
            ResourceAdapter ra = metadataListView.getSelectionModel().getSelectedItem();
            navState.setSelectedParent(ra);
        }
    }
}
