package gndata.app.ui.calendar;

import gndata.app.state.CalendarState;
import gndata.app.state.ProjectState;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.ResourceBundle;


public class TimelineCtrl implements Initializable {

    @FXML
    private BorderPane view;

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
