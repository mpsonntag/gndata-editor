package gndata.app.ui.query;

import java.util.List;
import javafx.collections.*;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import com.hp.hpl.jena.rdf.model.RDFNode;
import gndata.app.state.QueryState;
import gndata.app.ui.util.RDFTableItem;


public class TablePane extends TableView<RDFTableItem> {

    public TablePane(QueryState qs) {
        super();

        TableColumn<RDFTableItem,String> c1 = new TableColumn<>("Predicate");
        c1.setCellValueFactory(new PropertyValueFactory("predicate"));
        TableColumn<RDFTableItem,String> c2 = new TableColumn<>("Literal Value");
        c2.setCellValueFactory(new PropertyValueFactory("literal"));
        getColumns().setAll(c1, c2);

        qs.getSelectedNode().addListener((obs, odlVal, newVal) -> fillItems(newVal));
    }

    public void fillItems(RDFNode node) {
        List<RDFTableItem> items = RDFTableItem.buildTableItems(node);

        if (items.size() > 0) {
            items.sort((a, b) -> a.getPredicate().compareTo(b.getPredicate()));

            ObservableList<RDFTableItem> observableData = FXCollections.observableArrayList(items);
            SortedList<RDFTableItem> sortedData = new SortedList<>(observableData);

            // sort items by predicate value
            sortedData.setComparator((a, b) -> a.getPredicate().compareTo(b.getPredicate()));
            sortedData.comparatorProperty().bind(comparatorProperty());

            setItems(sortedData);
        } else {
            setItems(null);
        }
    }
}
