package gndata.app.ui.calendar;

import java.net.URL;
import java.util.*;
import javax.inject.Inject;
import javafx.fxml.*;
import javafx.scene.chart.*;

import gndata.app.state.*;


public class TimelineCtrl implements Initializable {

    @FXML
    private AreaChart timeline;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    private final ProjectState ps;
    private final CalendarState cs;

    @Inject
    public TimelineCtrl(ProjectState ps, CalendarState cs) {
        this.ps = ps;
        this.cs = cs;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setData();

        // TODO - update CalendarState with date selection
    }

    private void setData() {
        XYChart.Series series= new XYChart.Series();

        // TODO - fetch actual numbers from ps.metadataService

        Random ran = new Random();
        for (int i = 1; i <= 31; i++) {
            series.getData().add(new XYChart.Data(i, ran.nextInt(10)));
        }

        timeline.getData().setAll(series);
    }
}
