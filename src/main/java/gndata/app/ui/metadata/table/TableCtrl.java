package gndata.app.ui.metadata.table;

import com.hp.hpl.jena.rdf.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;


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

    private void fillItems(RDFNode selectedItem) {
        ObservableList<TableItem> items = FXCollections.observableArrayList();

        if (selectedItem != null && selectedItem.isResource()) {
            Resource r = selectedItem.asResource();

            StmtIterator iter = r.listProperties();
            while (iter.hasNext()) {
                Statement st = iter.nextStatement();

                if (st.getObject().isLiteral()) {
                    Literal l = st.getObject().asLiteral();

                    String predicate = st.getPredicate().getLocalName();
                    String literal = l.getValue().toString();
                    String type = l.getDatatype() != null ? l.getDatatype().getJavaClass().getSimpleName() : "";

                    items.add(new TableItem(predicate, literal, type));
                }
            }
        }

        tableView.setItems(items);
    }

    public void bindTo(TreeView<RDFNode> treeView) {
        treeView.getSelectionModel()
            .selectedItemProperty()
            .addListener((observable, oldVal, selectedItem) -> {
                this.fillItems(selectedItem.getValue());
            });

        // TODO add listener for tree destruction - items clean up
    }
}
