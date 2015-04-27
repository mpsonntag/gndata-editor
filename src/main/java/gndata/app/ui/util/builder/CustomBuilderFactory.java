package gndata.app.ui.util.builder;

import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.*;
import javafx.util.Builder;
import javafx.util.BuilderFactory;

/**
 * Builder factory used for customized creation of fxml controls.
 *
 * TODO this class is not used at the moment
 */
public class CustomBuilderFactory implements BuilderFactory {

    private BuilderFactory defaultFactory;

    public CustomBuilderFactory() {
        defaultFactory = new JavaFXBuilderFactory();
    }

    @Override
    public Builder<?> getBuilder(Class<?> cls) {
        if (cls == TextFormatter.class) {
            return new TextFormatterBuilder<>();
        } else {
            return defaultFactory.getBuilder(cls);
        }
    }

}
