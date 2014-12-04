package gndata.app.ui.filebrowser;

import java.net.URL;
import java.nio.file.*;
import java.util.*;
import javax.inject.Inject;
import javafx.beans.value.*;
import javafx.collections.ListChangeListener;
import javafx.fxml.*;
import javafx.scene.control.*;

import gndata.app.state.*;
import org.controlsfx.control.SegmentedButton;

/**
 * Created by msonntag on 02.12.14.
 */
public class FileNavigationCtrl implements Initializable {

    @FXML
    private SegmentedButton navBar;

    private final ProjectState projectState;
    private final FileNavigationState navState;

    @Inject
    public FileNavigationCtrl (FileNavigationState navState, ProjectState projectState) {

        this.projectState = projectState;
        this.navState = navState;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        navState.getNavigationPath().addListener(new NavPathListener());
        navState.selectedParentProperty().addListener(new SelectedParentListener());
        navBar.getToggleGroup().selectedToggleProperty().addListener(new ToggleGroupListener());

        // TODO initialize with first favorite in favorite panel
        projectState.configProperty().addListener(
                (b, o, n) -> navState.getNavigationPath().add(Paths.get(projectState.getConfig().getProjectPath()))
        );
    }

    private class NavPathListener implements ListChangeListener<Path> {

        @Override
        public void onChanged(Change<? extends Path> c) {
            List<Path> l = new ArrayList(c.getList());
            if (! l.isEmpty()) {
                navBar.getButtons().clear();

                l.stream()
                        .map(p -> new ToggleButton(p.getFileName().toString()))
                        .forEach(tb -> navBar.getButtons().add(tb));

                navState.setSelectedParent(l.get(l.size() - 1));
            }
        }
    }

    private class SelectedParentListener implements ChangeListener<Path> {

        @Override
        public void changed(ObservableValue<? extends Path> observable, Path oldValue, Path newValue) {
            if (oldValue == newValue) {
                return;
            }
            if (! projectState.getFileService().isFileInProject(newValue)) {
                return;
            }

            int position = navState.getNavigationPath().lastIndexOf(newValue);
            if (position < 0) {

                //TODO check if newValue is child of one of the elements in NavigationPath

                navState.getNavigationPath().clear();
                navState.getNavigationPath().add(newValue);
                position = 0;
            }

            ToggleButton toggle = navBar.getButtons().get(position);
            navBar.getToggleGroup().selectToggle(toggle);
        }
    }

    private class ToggleGroupListener implements ChangeListener<Toggle> {

        @Override
        public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
            if (newValue == null || oldValue == newValue) {
                return;
            }

            int position = navBar.getButtons().indexOf(newValue);
            navState.setSelectedParent(navState.getNavigationPath().get(position));
        }
    }
}