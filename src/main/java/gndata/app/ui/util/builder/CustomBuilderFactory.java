package gndata.app.ui.util.builder;

import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
        if (cls == TextField.class) {
            return new TextFieldBuilder();
        } else if (cls == TextArea.class) {
            return new TextAreaBuilder();
        } else {
            return defaultFactory.getBuilder(cls);
        }
    }

}
