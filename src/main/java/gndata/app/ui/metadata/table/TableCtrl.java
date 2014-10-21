package gndata.app.ui.metadata.table;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import com.hp.hpl.jena.rdf.model.*;


/**
 * Controller for the table to view metadata items.
 */
public class TableCtrl {

    @FXML
    private BorderPane tablePane;

    @FXML
    private TableView<TableItem> tableView;

    public TableCtrl() {}

    public void initialize() {}

    public void fillItems(RDFNode node) {
        ObservableList<TableItem> items = FXCollections.observableArrayList();

        if (node != null && node.isResource()) {
            Resource r = node.asResource();

            StmtIterator iter = r.listProperties();
            while (iter.hasNext()) {
                Statement st = iter.nextStatement();

                if (st.getObject().isLiteral()) {
                    Property p = st.getPredicate();
                    Literal l = st.getObject().asLiteral();

                    items.add(new TableItem(p, l));
                }
            }
        }

        tableView.setItems(items);
    }
}
