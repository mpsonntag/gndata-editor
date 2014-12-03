package gndata.app.ui.query;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;


public class QueryPane extends GridPane {

    private Button addMore;

    public QueryPane() {
        super();

        addMore = new Button("+");
        addMore.setOnAction(e -> addQueryRow());
        GridPane.setConstraints(addMore, 3, 0);
        getChildren().addAll(addMore);

        addQueryRow();
    }

    private void addQueryRow() {
        int rowIndex = GridPane.getRowIndex(addMore);

        GridPane.setRowIndex(addMore, rowIndex + 1);

        TextField to = new TextField();
        TextField tp = new TextField();
        TextField ts = new TextField();
        Button remRow = new Button("-");

        GridPane.setConstraints(to, 0, rowIndex);
        GridPane.setConstraints(tp, 1, rowIndex);
        GridPane.setConstraints(ts, 2, rowIndex);
        GridPane.setConstraints(remRow, 3, rowIndex);

        getChildren().addAll(to, tp, ts, remRow);

        remRow.setOnAction(e -> {
            int index = (int) remRow.getProperties().get("gridpane-row");

            List<Node> toRemove = new ArrayList<Node>();
            getChildren().forEach(node -> {
                if (GridPane.getRowIndex(node) == index &&
                        getChildren().size() > 5) {
                    toRemove.add(node);
                }
            });

            getChildren().removeAll(toRemove);
        });
    }

    public String getCurrentQuery() {
        return "foo";
    }
}
