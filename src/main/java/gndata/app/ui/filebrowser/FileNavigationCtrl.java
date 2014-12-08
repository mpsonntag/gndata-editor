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
import gndata.lib.srv.*;
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

        projectState.configProperty().addListener(
                (b, o, n) -> navState.getNavigationPath().add(navState.getFavoriteFolders().get(0))
        );
    }

    private class NavPathListener implements ListChangeListener<FileAdapter> {

        @Override
        public void onChanged(Change<? extends FileAdapter> c) {
            List<FileAdapter> l = new ArrayList(c.getList());
            if (! l.isEmpty()) {
                navBar.getButtons().clear();

                l.stream()
                        .map(p -> new ToggleButton(p.getFileName().toString()))
                        .forEach(tb -> navBar.getButtons().add(tb));

                navState.setSelectedParent(l.get(l.size() - 1));
            }
        }
    }

    private class SelectedParentListener implements ChangeListener<FileAdapter> {

        @Override
        public void changed(ObservableValue<? extends FileAdapter> observable, FileAdapter oldValue, FileAdapter newValue) {
            if (oldValue == newValue) {
                return;
            }
            if (! projectState.getFileService().isFileInProject(newValue)) {
                return;
            }

            int pos = navState.getNavigationPath().lastIndexOf(newValue);

            if (pos < 0) {

                for (pos = navState.getNavigationPath().size() - 1; pos >= 0; pos--) {
                    FileAdapter curr = navState.getNavigationPath().get(pos);
                    if (curr.hasChild(newValue))
                        break;

                    navState.getNavigationPath().remove(pos);
                }

                navState.getNavigationPath().add(newValue);
                pos++;
            }

            ToggleButton toggle = navBar.getButtons().get(pos);
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