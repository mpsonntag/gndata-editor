package gndata.app.ui.query;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import gndata.app.state.QueryState;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;


public class ListPane extends ListView<Statement> {

    public ListPane(QueryState qs) {
        super();

        // update State when item is selected
        getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) ->
            qs.setSelectedNode(newVal != null ? newVal.getSubject() : null)
        );

        // update list contents if the selection changed
        qs.getSelectedModel().addListener((obs, odlVal, newVal) ->
                updateSelection(qs.getSelectedModel().get()));
    }

    public void updateSelection(Model selection) {
        ObservableList<Statement> lst = FXCollections.observableArrayList();
        if (selection != null) {
            lst.setAll(selection.listStatements().toList());
        }

        getItems().setAll(lst);
    }
}
