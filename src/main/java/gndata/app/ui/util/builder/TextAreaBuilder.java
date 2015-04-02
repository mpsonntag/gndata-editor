package gndata.app.ui.util.builder;

import javafx.scene.control.TextArea;

/**
 * Created by stoewer on 01.04.15.
 */
public class TextAreaBuilder extends TextInputControlBuilder<TextArea> {

    @Override
    public TextArea build() {
        TextArea ta = new TextArea();

        return configure(ta);
    }

}
