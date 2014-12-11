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
import gndata.app.state.*;
import gndata.app.ui.util.*;
import gndata.lib.srv.*;

/**
 * Controller for the metadata list.
 */
public class MetadataListCtrl implements Initializable {


    @FXML
    private ListView<ResourceAdapter> metadataListView;
    @FXML
    private TextField filterTextField;

    private final ProjectState projectState;
    private final MetadataNavState navState;
    private final StringProperty filter;
    private final ObservableList<ResourceAdapter> filteredList;
    private final List<ResourceAdapter> unfilteredList;

    @Inject
    public MetadataListCtrl(ProjectState projectState, MetadataNavState navState) {
        this.projectState = projectState;
        this.navState = navState;

        filter = new SimpleStringProperty();
        filteredList = FXCollections.observableArrayList();
        unfilteredList = new ArrayList<>();

        this.navState.selectedParentProperty().addListener(new SelectedParentListener());
        filter.addListener((p, o, n) -> applyFilter(n));

        this.navState.searchStringProperty().addListener(new SearchStringHandler());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filterTextField.textProperty().bindBidirectional(filter);

        metadataListView.setItems(filteredList);
        navState.selectedNodeProperty().bind(metadataListView.getSelectionModel().selectedItemProperty());

        metadataListView.addEventHandler(MouseEvent.MOUSE_CLICKED, new ListNavigationHandler());
        metadataListView.setCellFactory(ra -> new MetadataListCell());
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

    /**
     * Listen for selected parent changes.
     */
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

            navState.setShowBrowsingResults(true);
        }
    }


    /**
     * Listen for double clicks on the list.
     */
    private class ListNavigationHandler extends DoubleClickHandler {

        @Override
        public void handleDoubleClick(MouseEvent mouseEvent) {
            ResourceAdapter ra = metadataListView.getSelectionModel().getSelectedItem();
            navState.setSelectedParent(ra);
        }
    }


    /**
     * Listen for changes in the search string property of the navigation state.
     */
    private class SearchStringHandler implements ChangeListener<String> {

        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            MetadataService ms = projectState.getMetadata();

            if (ms != null) {
                unfilteredList.clear();
                unfilteredList.addAll(
                        ms.streamSearchResults(navState.getSearchString())
                            .map(r -> new ResourceAdapter(r, null))
                            .collect(Collectors.toList())
                );

                String fltr = filter.get();
                if (fltr == null || fltr.equals("")) {
                    applyFilter(null); // just filter with null
                } else {
                    filter.set(null); // reset the filter (triggers filtering)
                }
            }
        }
    }


    /**
     * A list cell for lists showing a {@link ResourceAdapter}.
     */
    private class MetadataListCell extends TwoLineListCell<ResourceAdapter> {

        @Override
        protected void update(ResourceAdapter item, boolean empty) {
            if (! empty) {
                lineOne.set(item.getFileName());
                lineTwo.set(item.getInfo());
            } else {
                lineOne.set(null);
                lineTwo.set(null);
            }
        }
    }
}
