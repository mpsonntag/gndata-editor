package gndata.app.ui.util.builder;

import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Builder;
import javafx.util.BuilderFactory;

/**
 * Created by stoewer on 01.04.15.
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
