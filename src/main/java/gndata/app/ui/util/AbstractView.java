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

import gndata.app.ui.util.builder.CustomBuilderFactory;
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
        loader = new FXMLLoader(NameConventions.fxmlResource(getClass()), null, new CustomBuilderFactory());
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

        Optional<URL> styleURL = NameConventions.optionalStyleResource(getClass());
        if (styleURL.isPresent()) {
            p.getStylesheets().add(styleURL.get().toExternalForm());
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

}
