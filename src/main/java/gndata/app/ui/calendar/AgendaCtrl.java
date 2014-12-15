package gndata.app.ui.calendar;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javafx.collections.*;
import javafx.fxml.*;

import gndata.app.state.*;
import gndata.lib.srv.ResourceEvent;
import jfxtras.scene.control.agenda.Agenda;


public class AgendaCtrl implements Initializable {

    @FXML
    private Agenda agenda;

    private ObservableList<ResourceEvent> events;

    private final ProjectState ps;
    private final CalendarState cs;

    @Inject
    public AgendaCtrl(ProjectState ps, CalendarState cs) {
        this.ps = ps;
        this.cs = cs;

        events = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        agenda.appointments().setAll(events);

        cs.getSelectedDate().addListener(dt -> updateEvents());
    }

    private void updateEvents() {

        // TODO select resources by cs.getSelectedDate()

        if (ps.getMetadata() != null) {
            events.setAll(ps.getMetadata().getAnnotations()
                    .listStatements()
                    .toList().subList(0, 20)
                    .stream()
                    .map(st -> new ResourceEvent(st.getSubject()))
                    .collect(Collectors.toList()));
        }
    }
}
