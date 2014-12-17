// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javafx.fxml.*;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

import gndata.app.ui.calendar.CalendarView;
import gndata.app.ui.dash.DashboardView;
import gndata.app.ui.filebrowser.FileBrowserView;
import gndata.app.ui.metadata.MetadataBrowserView;
import gndata.app.ui.notes.NotesView;
import gndata.app.ui.query.QueryView;

/**
 * Controller for the main application window.
 */
public class MainCtrl implements Initializable {

    @FXML
    public BorderPane view;
    @FXML
    public Tab dashboard;
    @FXML
    public Tab metadata;
    @FXML
    public Tab calendar;
    @FXML
    public Tab query;
    @FXML
    public Tab files;
    @FXML
    public Tab notes;


    private MenuView menuView;
    private MetadataBrowserView metadataView;
    private QueryView queryView;
    private CalendarView calendarView;
    private FileBrowserView fileBrowserView;
    private DashboardView dashView;
    private NotesView notesView;

    @Inject
    public MainCtrl(MenuView menuView, MetadataBrowserView metadataView,
                    QueryView queryView, CalendarView calendarView,
                    FileBrowserView fileBrowserView, DashboardView dashView,
                    NotesView notesView) {

        this.menuView = menuView;
        this.metadataView = metadataView;
        this.queryView = queryView;
        this.calendarView = calendarView;
        this.fileBrowserView = fileBrowserView;
        this.dashView = dashView;
        this.notesView = notesView;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // top Menu bar
            view.setTop(menuView.getScene());

            metadata.setContent(metadataView.getScene());
            query.setContent(queryView.getScene());
            calendar.setContent(calendarView.getScene());
            files.setContent(fileBrowserView.getScene());
            dashboard.setContent(dashView.getScene());
            notes.setContent(notesView.getScene());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
