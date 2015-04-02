package gndata.app.ui.util.builder;

import javafx.scene.control.TextField;

/**
 * Created by stoewer on 01.04.15.
 */
public class TextFieldBuilder extends TextInputControlBuilder<TextField> {

    @Override
    public TextField build() {
        TextField tf = new TextField();

        return configure(tf);
    }

}
