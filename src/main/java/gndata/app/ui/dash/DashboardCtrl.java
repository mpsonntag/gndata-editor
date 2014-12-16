package gndata.app.ui.dash;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;

import gndata.app.html.PageCtrl;

/**
 * Created by stoewer on 16.12.14.
 */
public class DashboardCtrl extends PageCtrl {

    @FXML
    private WebView webView;

    @Override
    public WebView getWebView() {
        return webView;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        DashboardData model = new DashboardData("Cool name", "Cool description");

        getPage().applyModel(model);
    }
}
