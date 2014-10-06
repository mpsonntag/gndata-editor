package gndata.app.ui.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

/**
 * Base class for views with convention over configuration pattern.
 *
 * If not provided to the constructor, the view loads a fxml file with the same name as the
 * view class (but .fxml extension) as the scene and applies a css file also with the same
 * name (but .css extension) if such a file is present.
 * <br />
 * For example a FooView class would try to load FooView.fxml and FooView.css.
 */
public abstract class AbstractView {

    private final URL styleURL;
    private final FXMLLoader loader;

    /**
     * Creates a new view and uses convention over configuration for the fxml and css files.
     */
    public AbstractView() {
        this(Optional.empty(), Optional.empty());
    }

    /**
     * Creates a new view and uses convention over configuration for the fxml and css file only
     * when they are not provided.
     *
     * @param viewURL   The URL to the fxml file. If empty the URL will be inferred from the
     *                  class name.
     * @param styleURL  The URL to the css file. If empty the URL will be inferred from the
     *                  class name.
     */
    public AbstractView(Optional<URL> viewURL, Optional<URL> styleURL) {
        this.styleURL = styleURL.orElse(generateStyleURL());
        loader = new FXMLLoader(viewURL.orElse(generateViewURL()));
    }

    /**
     * Loads the scene for this view.
     *
     * @return The loaded scene.
     * @throws IOException If the loading fails.
     */
    public Parent getScene() throws IOException {
        Parent p = loader.load();

        if (styleURL != null) {
            p.getStylesheets().add(styleURL.toExternalForm());
        }

        return p;
    }

    /**
     * Getter for the fxml loader.
     *
     * @return The loader of the view.
     */
    public FXMLLoader getLoader() {
        return loader;
    }

    /**
     * Generates an URL that points to the corresponding fxml file.
     *
     * @return An URL to the fxml file.
     */
    private URL generateViewURL() {
        String path = "/" + getClass().getCanonicalName().replace('.', '/') + ".fxml";
        return getClass().getResource(path);
    }

    /**
     * Generates an URL that points to the corresponding css file.
     *
     * @return An URL to the css file.
     */
    private URL generateStyleURL() {
        String path = "/" + getClass().getCanonicalName().replace('.', '/') + ".css";
        return getClass().getResource(path);
    }
}
