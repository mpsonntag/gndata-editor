package gndata.app.ui.main;

import gndata.app.state.AppState;
import javafx.event.ActionEvent;

import javax.inject.Inject;

public class MenuCtrl {

    @Inject
    private AppState state;

    public void openProject(ActionEvent event)
    {
        System.out.println("openProject");
    }

    public void createProject(ActionEvent event)
    {
        System.out.println("createProject");
    }

    public void exit(ActionEvent e)
    {
        System.out.println("Called exit");
        state.setRunning(false);
    }
}
