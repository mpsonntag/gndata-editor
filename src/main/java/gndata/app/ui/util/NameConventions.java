package gndata.app.ui.util;

import java.net.URL;
import java.util.Optional;

/**
 * Helper class for getting names for controllers, views, fxml files and templates
 * according to naming convention rules.
 */
public class NameConventions {

    private static final String ctrlEnd = "Ctrl";
    private static final String viewEnd = "View";
    private static final String fxmlExt = ".fxml";
    private static final String vtlExt  = ".vtl";
    private static final String cssExt  = ".css";
    private static final String[] endings = {ctrlEnd, viewEnd};


    /**
     * Get the name of a velocity template file for a certain class. The template file
     * is determined using the following naming conventions. If the class is a inner 
     * class the name of the most outer class is used to determine the template.
     * 
     * <ol>
     *     <li>foo.Foo --> foo/FooView.vtl</li>
     *     <li>foo.FooView --> foo/FooView.vtl</li>
     *     <li>foo.FooCtrl --> foo/FooView.vtl</li>
     * </ol>
     * 
     * @param cls   The class to get the template for.
     *
     * @return The path to the template file.
     */
    public static String templatePath(Class cls) {
        return viewClassPath(cls) + vtlExt;
    }

    /**
     * Get the resource of a template for a certain class.
     *
     * @param cls   The class to get the template resource for.
     *
     * @return The url to the template.
     *
     * @throws RuntimeException If the resource does not exist.
     * @see #templatePath(Class)
     */
    public static URL templateResource(Class cls) throws RuntimeException {
        String path = templatePath(cls);
        URL url = ClassLoader.getSystemResource(path);

        if (url == null)
            throw new RuntimeException("Unable to find resource: " + path);

        return url;
    }

    /**
     * Get the name of a fxml file for a certain class. The fxml file
     * is determined using the following naming conventions. If the class is a inner
     * class the name of the most outer class is used to determine the fxml file.
     *
     * <ol>
     *     <li>foo.Foo --> foo/FooView.fxml</li>
     *     <li>foo.FooView --> foo/FooView.fxml</li>
     *     <li>foo.FooCtrl --> foo/FooView.fxml</li>
     * </ol>
     *
     * @param cls   The class to get the fxml file for.
     *
     * @return The path to the fxml file.
     */
    public static String fxmlPath(Class cls) {
        return viewClassPath(cls) + fxmlExt;
    }

    /**
     * Get the resource of a fxml for a certain class.
     *
     * @param cls   The class to get the fxml resource for.
     *
     * @return The url to the fxml file.
     *
     * @throws RuntimeException If the resource does not exist.
     * @see #fxmlPath(Class)
     */
    public static URL fxmlResource(Class cls) throws RuntimeException {
        String path = fxmlPath(cls);
        URL url = ClassLoader.getSystemResource(path);

        if (url == null)
            throw new RuntimeException("Unable to find resource: " + path);

        return url;
    }

    /**
     * Get the name of a css file for a certain class. The css file
     * is determined using the following naming conventions. If the class is a inner
     * class the name of the most outer class is used to determine the css file.
     *
     * <ol>
     *     <li>foo.Foo --> foo/FooView.css</li>
     *     <li>foo.FooView --> foo/FooView.css</li>
     *     <li>foo.FooCtrl --> foo/FooView.css</li>
     * </ol>
     *
     * @param cls   The class to get the css file for.
     *
     * @return The path to the css file.
     */
    public static String stylePath(Class cls) {
        return viewClassPath(cls) + cssExt;
    }

    /**
     * Get the resource of a css for a certain class.
     *
     * @param cls   The class to get the css resource for.
     *
     * @return The url to the css file.
     *
     * @throws RuntimeException If the resource does not exist.
     * @see #fxmlPath(Class)
     */
    public static URL styleResource(Class cls) throws RuntimeException {
        String path = stylePath(cls);
        URL url = ClassLoader.getSystemResource(path);

        if (url == null)
            throw new RuntimeException("Unable to find resource: " + path);

        return url;
    }

    /**
     * Get the resource of a css for a certain class.
     *
     * @param cls   The class to get the css resource for.
     *
     * @return Optional url to the css resource.
     *
     * @see #fxmlPath(Class)
     */
    public static Optional<URL> optionalStyleResource(Class cls) throws RuntimeException {
        String path = stylePath(cls);
        URL url = ClassLoader.getSystemResource(path);

        return Optional.ofNullable(url);
    }

    /**
     * Get the path to a view class according to the following naming conventions.
     * If the class is a inner class the name of te most outer class is used to
     * determine the view.
     *
     * <ol>
     *     <li>foo.Foo --> foo/FooView</li>
     *     <li>foo.FooView --> foo/FooView</li>
     *     <li>foo.FooCtrl --> foo/FooView</li>
     * </ol>
     *
     * @param cls   The class to get the view path for.
     *
     * @return A path to the corresponding view class.
     */
    public static String viewClassPath(Class cls) {
        return basePath(cls.getName()) + viewEnd;
    }

    /**
     * Get the view class for a certain other class. The rules are the same as
     * for {@link #viewClassPath(Class)}.
     *
     * @param cls   The class to get the view class for.
     *
     * @return The corresponding view class.
     *
     * @throws ClassNotFoundException If the class does not exist.
     * @see #viewClassPath(Class)
     */
    public static Class viewClass(Class cls) throws ClassNotFoundException {
        String name = baseName(cls.getName()) + viewEnd;

        return Class.forName(name);
    }

    /**
     * Get the path to a controller class according to the following naming conventions.
     * If the class is a inner class the name of te most outer class is used to
     * determine the controller.
     *
     * <ol>
     *     <li>foo.Foo --> foo/FooCtrl</li>
     *     <li>foo.FooView --> foo/FooCtrl</li>
     *     <li>foo.FooCtrl --> foo/FooCtrl</li>
     * </ol>
     *
     * @param cls   The class to get the view path for.
     *
     * @return A path to the corresponding view class.
     */
    public static String ctrlClassPath(Class cls) {
        return basePath(cls.getName()) + ctrlEnd;
    }

    /**
     * Get the controller class for a certain other class. The rules are the same as
     * for {@link #ctrlClassPath(Class)}.
     *
     * @param cls   The class to get the controller class for.
     *
     * @return The corresponding controller class.
     *
     * @throws ClassNotFoundException If the class does not exist.
     * @see #ctrlClassPath(Class)
     */
    public static Class ctrlClass(Class cls) throws ClassNotFoundException {
        String name = baseName(cls.getName()) + ctrlEnd;

        return Class.forName(name);
    }

    // private helper methods

    /**
     * Get the base path for a certain canonical name of a class.
     */
    private static String basePath(String canonicalName) {
        String baseName = baseName(canonicalName);

        return baseName.replace('.', '/');
    }


    /**
     * Get the base name for a certain canonical name of a class.
     */
    private static String baseName(String canonicalName) {
        int index = canonicalName.indexOf('$');

        if (index > 0)
            canonicalName = canonicalName.substring(0, index);

        for (String end : endings) {
            if (canonicalName.endsWith(end)) {
                canonicalName = canonicalName.substring(0, canonicalName.length() - end.length());
                break;
            }
        }

        return canonicalName;
    }
}
