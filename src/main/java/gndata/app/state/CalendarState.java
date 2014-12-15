// Copyright (c) 2014, German Neuroinformatics Date (G-Date)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.state;

import java.time.LocalDate;
import java.util.ArrayList;
import javax.inject.Singleton;
import javafx.beans.property.*;
import javafx.collections.*;

import gndata.lib.srv.ResourceAdapter;

/**
 * Class that provides state information about selected date and object types
 * to display.
 */
@Singleton
public class CalendarState {

    private ObjectProperty<LocalDate> selectedDate;
    private ObservableList<ResourceAdapter> selectedTypes;

    public CalendarState() {
        selectedDate = new SimpleObjectProperty<>();
        selectedTypes = FXCollections.observableList(new ArrayList<ResourceAdapter>());
    }

    public ObjectProperty<LocalDate> getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate.set(selectedDate);
    }

    public ObservableList<ResourceAdapter> getSelectedTypes() {
        return selectedTypes;
    }
}
