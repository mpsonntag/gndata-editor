// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.filebrowser;

import java.net.URL;
import java.nio.file.*;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.control.*;

import com.google.inject.Inject;
import gndata.app.state.*;
import gndata.lib.srv.LocalFile;

/**
 * Controller for {@link FileFavoritesView}
 */
public class FileFavoritesCtrl implements Initializable {

    @FXML
    private ListView<LocalFile> fileFavorites;
    @FXML
    private Button openFileFavoriteHandling;

    private final FileNavigationState navState;
    private final ProjectState projectState;

    @Inject
    public FileFavoritesCtrl(ProjectState projectState, FileNavigationState navState) {

        this.projectState = projectState;
        this.navState = navState;

        // handle what happens if a project is opened
        this.projectState.configProperty().addListener((p, o, n) -> {
            if (n == null)
                return;

            // load favorite folders from project
            Path path = Paths.get(n.getProjectPath());

            // reset any previous favorites if a project is opened
            this.navState.getFavoriteFolders().clear();

            // TODO load actual file favorites instead of the following 2 lines of dummy favorites
            this.navState.getFavoriteFolders().add(new LocalFile(path.resolve(Paths.get("dataExperimentators","Humer Ingrid","relevantPapers"))));
            this.navState.getFavoriteFolders().add(new LocalFile(path));
            this.navState.getFavoriteFolders().add(new LocalFile(path.resolve(Paths.get("dataMain","behavior rw"))));
            this.navState.getFavoriteFolders().add(new LocalFile(path.resolve(Paths.get("dataMain","results"))));

            // set navState selected parent to the first favorite folder
            iterateToProjectRoot(this.navState.getFavoriteFolders().get(0));
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        fileFavorites.setItems(navState.getFavoriteFolders());

        fileFavorites.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> {
            if (n == null)
                return;

            // if selected favorite is not the same as the project root,
            // construct navigation from root to selected folder
            iterateToProjectRoot(n);
        });

        // Deselect file favorite, if the selected file favorite is not the same as the
        // current selected parent of the navigation state any longer
        // deselecting favorite leads to an undetectable indexOutOfBoundsException
        // keep commented out until there's time to work this out. maybe with focus property
        /*this.navState.selectedParentProperty().addListener((p, o, n) -> {
            if (n == null || n.equals(fileFavorites.getSelectionModel().getSelectedItem())) {
                return;
            }
            fileFavorites.getSelectionModel().clearSelection();
        });*/
    }

    /**
     * This method iterates over the parent folders of a LocalFile
     * until it reaches the project root and constructs the
     * navigation path from this iteration.
     * LocalFiles outside of the project path will not be handled
     *
     * @param currLocalFile
     */
    private void iterateToProjectRoot(LocalFile currLocalFile) {

        Path projectPath = Paths.get(projectState.getConfig().getProjectPath());
        // check if path of LocalFile is actually within the project path
        if (currLocalFile.isChildOfAbsolutePath(projectPath)){

            // check if the parent is the project root, if not start another iteration with the parent
            if (!currLocalFile.hasPath(projectPath)) {
                Optional<LocalFile> parent = currLocalFile.getParent();
                if (parent.isPresent()) {
                    iterateToProjectRoot(parent.get());
                }
            }
            // set selected parent iteratively to construct navigation path
            navState.setSelectedParent(currLocalFile);
        }
    }
}
