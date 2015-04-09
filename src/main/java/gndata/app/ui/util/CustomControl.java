package gndata.app.ui.util;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;

import gndata.app.ui.util.builder.CustomBuilderFactory;

/**
 * Base class for custom controls that define their layout using an fxml
 * file. The class uses standard naming conventions to infer the fxml and
 * stylesheet files from the class name of the control.
 *
 * The corresponding fxml document must have fx:root as top level scene
 * element.
 */
public abstract class CustomControl extends Region {

    /**
     * Default constructor which does not apply additional stylesheet.
     */
    public CustomControl() {
        this(new LinkedList<>());
    }

    /**
     * The constructor loads a corresponding fxml and css file according to the
     * default naming conventions {@link NameConventions} and sets itself
     * as root and controller of the loaded fxml scene.
     *
     * @param extraStyles   List with additional stylesheets.
     */
    public CustomControl(List<URL> extraStyles) {

        FXMLLoader loader = new FXMLLoader(NameConventions.fxmlResource(getClass()), null, new CustomBuilderFactory());

        loader.setRoot(this);
        loader.setController(this);

        Optional<URL> style = NameConventions.optionalStyleResource(getClass());
        if (style.isPresent()) {
            getStylesheets().add(style.get().toExternalForm());
        }

        for (URL url : extraStyles) {
            getStylesheets().add(url.toExternalForm());
        }

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
