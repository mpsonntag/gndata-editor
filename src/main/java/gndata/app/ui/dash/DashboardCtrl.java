package gndata.app.ui.dash;

import javax.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;

import gndata.app.html.PageCtrl;
import gndata.app.state.ProjectState;

/**
 * Controller for the dashboard.
 */
public class DashboardCtrl extends PageCtrl {

    @FXML
    private WebView webView;

    private ProjectState projectState;

    @Inject
    public DashboardCtrl(ProjectState projectState) {
        this.projectState = projectState;

        this.projectState.configProperty().addListener((p, o, n) -> {
            getPage().applyModel(new DashboardData());
        });
    }

    @Override
    public WebView getWebView() {
        return webView;
    }

    /**
     * Just a demo for accessing the controller from the page.
     */
    public void goToMetadata() {
        System.out.println("Go to metadata");
    }
}
