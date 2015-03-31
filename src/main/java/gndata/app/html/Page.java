package gndata.app.html;

import java.io.*;
import javafx.scene.web.WebView;

import netscape.javascript.JSObject;
import org.apache.velocity.*;

/**
 * MVC style html page handling. A page manages a {@link WebView} and a velocity {@link Template}.
 * The template ist used to render an applied model, the result of the rendering process is shown
 * as document of the provided WebView.
 */
public class Page {

    private static final String BOOTSTRAP = "META-INF/resources/webjars/bootstrap/3.3.0/css/bootstrap.min.css";
    private static final String ICONS = "icons";

    private final WebView webView;
    private Template view;

    /**
     * Create a new page.
     *
     * @param webView   The WebView to render in.
     * @param view      The template.
     */
    public Page(WebView webView, Template view) {
        this.webView = webView;
        this.view = view;
    }

    /**
     * Get a web view component showing the page.
     *
     * @return The web view.
     */
    public WebView getWebView() {
        return webView;
    }

    /**
     * Render a model in the page.
     *
     * @param model The model to render.
     */
    public void applyModel(Object model) {
        VelocityContext context = new VelocityContext();

        context.put("model", model);
        context.put("bootstrap", ClassLoader.getSystemResource(BOOTSTRAP));
        context.put("icons", ClassLoader.getSystemResource(ICONS));

        StringWriter writer = new StringWriter();

        view.merge(context, writer);
        String content = writer.toString();

        webView.getEngine().loadContent(content);
    }

    /**
     * Add a controller to the web engine.
     * The controller can be accessed in the view as variable 'controller'.
     *
     * @param controller The controller to apply.
     */
    public void applyController(Object controller) {
        JSObject window = (JSObject) webView.getEngine().executeScript("window");
        window.setMember("controller", controller);
    }

    /**
     * Create a page while loading the template with the provided name.
     *
     * @param webView   The WebView to render in.
     * @param viewName  The name of the template (as system resource).
     *
     * @return The created page object.
     *
     * @throws IOException If the loading of the template fails.
     */
    public static Page create(WebView webView, String viewName) throws IOException {
        Template view = VelocityHelper.getTemplateFromResource(viewName);

        return new Page(webView, view);
    }

}
