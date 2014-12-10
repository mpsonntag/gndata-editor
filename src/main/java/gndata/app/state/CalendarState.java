// Copyright (c) 2014, German Neuroinformatics Date (G-Date)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.state;

import gndata.lib.srv.ResourceAdapter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class that provides state information about selected date and object types
 * to display.
 */
@Singleton
public class CalendarState {

    private ObjectProperty<Date> selectedDate;
    private ObservableList<ResourceAdapter> selectedTypes;

    public CalendarState() {
        selectedDate = new SimpleObjectProperty<>();
        selectedTypes = FXCollections.observableList(new ArrayList<ResourceAdapter>());
    }

    public ObjectProperty<Date> getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate.set(selectedDate);
    }

    public ObservableList<ResourceAdapter> getSelectedTypes() {
        return selectedTypes;
    }
}
