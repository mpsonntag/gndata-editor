package gndata.app.ui.query;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;


public class ListPane extends ListView<Statement> {

    private Model selection;

    public void updateSelection(Model selection) {
        this.selection = selection;

        ObservableList<Statement> lst = FXCollections.observableArrayList();
        if (this.selection != null) {
            lst.setAll(this.selection.listStatements().toList());
        }

        getItems().setAll(lst);
    }
}
