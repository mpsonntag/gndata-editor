package gndata.app.ui.util.builder;

import java.util.function.UnaryOperator;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.util.*;

/**
 * Created by msonntag on 24.04.15.
 */
public class TextFormatterBuilder<T> implements Builder<TextFormatter<T>> {

    private StringConverter<T> valueConverter;
    private UnaryOperator<TextFormatter.Change> filter;

    @Override
    public TextFormatter<T> build() {
        return new TextFormatter<>(valueConverter, null, filter);
    }

    public StringConverter<T> getValueConverter() {
        return valueConverter;
    }

    public void setValueConverter(StringConverter<T> valueConverter) {
        this.valueConverter = valueConverter;
    }

    public UnaryOperator<Change> getFilter() {
        return filter;
    }

    public void setFilter(UnaryOperator<Change> filter) {
        this.filter = filter;
    }
}
