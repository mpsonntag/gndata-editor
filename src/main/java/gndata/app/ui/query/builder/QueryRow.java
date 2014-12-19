package gndata.app.ui.query.builder;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.Arrays;
import java.util.List;

public class QueryRow extends HBox {

    private TextField subject;
    private TextField predicate;
    private TextField object;
    private Button remove;

    public QueryRow(String subj, String pred, String obj) {
        super();

        subject = new TextField(subj);
        predicate = new TextField(pred);
        object = new TextField(obj);
        remove = new Button("-");

        getChildren().addAll(subject, predicate, object, remove);
    }

    public void onRemove(EventHandler<ActionEvent> onRemove) {
        remove.setOnAction(onRemove);
    }

    public List<String> getInput() {
        return Arrays.asList(subject.getText(), predicate.getText(), object.getText());
    }
}