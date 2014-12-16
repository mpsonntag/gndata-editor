package gndata.app.html;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;

/**
 * A controller that initializes a page.
 */
public abstract class  PageCtrl implements Initializable {

    private Page page;

    /**
     * The web view to initialize the page with.
     *
     * @return The web view f the page.
     */
    public abstract WebView getWebView();

    /**
     * The page object. Is ready after initialize.
     *
     * @return The page.
     */
    public Page getPage() {
        return page;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            page = Page.create(getWebView(), getDefaultTemplate());
            page.applyController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDefaultTemplate() {
        String path = "" + getClass().getCanonicalName().replace('.', '/');

        if (path.endsWith("Ctrl")) {
            path = path.substring(0, path.length() - 4);
        }

        return path + "View.vtl";
    }
}
