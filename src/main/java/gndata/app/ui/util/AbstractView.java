// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * Base class for views with convention over configuration pattern.
 *
 * The view loads a fxml file with the same name as the
 * view class (but .fxml extension) as the scene and applies a css file also with the same
 * name (but .css extension) if such a file is present.
 * <br />
 * For example a FooView class would try to load FooView.fxml and FooView.css.
 * <br />
 * In addition to the view specific stylesheet, a list of other stylesheets can be specified.
 */
public abstract class AbstractView {

    private final FXMLLoader loader;
    private List<URL> extraStyles;

    /**
     * Creates a new view and uses convention over configuration for the fxml
     * and css files.
     */
    public AbstractView() {
        extraStyles = new LinkedList<>();
        loader = new FXMLLoader(getDefaultView());
    }

    /**
     * Loads the scene for this view.
     * The method uses the default view and style to generate the parent scene
     * of the view and applies all extra styles to the scene.
     *
     * @return The loaded scene.
     * @throws IOException If the loading fails.
     */
    public Parent getScene() throws IOException {
        Parent p = loader.load();

        URL styleURL = getDefaultStyle();
        if (styleURL != null) {
            p.getStylesheets().add(styleURL.toExternalForm());
        }

        for (URL url : getExtraStyles()) {
            p.getStylesheets().add(url.toExternalForm());
        }

        return p;
    }

    /**
     * Get extra stylesheet URLs.
     *
     * @return List with URLs to additional stylesheets.
     */
    public List<URL> getExtraStyles() {
        return extraStyles;
    }

    /**
     * Set extra stylesheet URLs.
     *
     * @param extraStyles List with URLs to additional stylesheets.
     */
    public void setExtraStyles(List<URL> extraStyles) {
        this.extraStyles = extraStyles;
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
     * Get an URL that points to the default fxml file.
     * The default view fxml file is derived from the canonical class name of the
     * respective view.
     *
     * @return An URL to the fxml file.
     */
    public URL getDefaultView() {
        String path = "/" + getClass().getCanonicalName().replace('.', '/') + ".fxml";
        return getClass().getResource(path);
    }

    /**
     * Get an URL that points to the default css file.
     * The default style css file is derived from the canonical class name of the
     * respective view.
     *
     * @return An URL to the css file.
     */
    public URL getDefaultStyle() {
        String path = "/" + getClass().getCanonicalName().replace('.', '/') + ".css";
        return getClass().getResource(path);
    }
}
