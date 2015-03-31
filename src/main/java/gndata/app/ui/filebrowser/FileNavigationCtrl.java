// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.filebrowser;

import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.scene.control.TextField;

import gndata.app.state.*;
import gndata.app.ui.util.*;
import gndata.lib.srv.LocalFile;

/**
 * Controller for FileNavigation
 */
public class FileNavigationCtrl implements Initializable {
    @FXML
    private TogglePane togglePane;
    @FXML
    private TextField searchField;
    @FXML
    private BreadCrumbNav<LocalFile> navBar;

    private final FileNavigationState navState;
    private final BooleanProperty showBreadCrumbs;

    @Inject
    public FileNavigationCtrl (FileNavigationState navState) {

        this.navState = navState;
        showBreadCrumbs = new SimpleBooleanProperty(true);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // ensure bidirectional changes in navigation bar and navigation state
        navBar.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> navState.setSelectedParent(n));
        navState.selectedParentProperty().addListener((p, o, n) -> navBar.getSelectionModel().select(n));

        togglePane.showFirstProperty().bindBidirectional(showBreadCrumbs);

    }

    public void toggleNavBar() {
        showBreadCrumbs.set(! showBreadCrumbs.get());
    }

    public void goBack() {
        // TODO implement
        System.out.println("FileNavCtrl: goBack");
    }

    public void goForward() {
        // TODO implement
        System.out.println("FileNavCtrl: goForward");
    }
}
