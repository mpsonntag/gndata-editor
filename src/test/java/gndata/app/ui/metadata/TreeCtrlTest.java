package gndata.app.ui.metadata;

import gndata.app.state.ProjectState;
import gndata.app.ui.metadata.tree.TreeCtrl;
import gndata.lib.config.ProjectConfig;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;


public class TreeCtrlTest {

    private static final Path tmpPath = Paths.get(System.getProperty("java.io.tmpdir"), "test-project");

    ProjectState ps = new ProjectState();

    @Before
    public void setUp() throws Exception {
        ProjectConfig config = ProjectConfig.load(tmpPath.toString());
        config.setName("MyName");
        config.setDescription("MyDescription");

        ps.setConfig(config);
    }

    @After
    public void tearDown() throws Exception {
        if (Files.exists(tmpPath)) {
            FileUtils.deleteDirectory(tmpPath.toFile());
        }
    }

    @Test
    public void testListen() throws Exception {
        TreeCtrl ctrl = new TreeCtrl(ps);
        assertNull(ctrl.getTree());

        /* TODO find the way to inject FXML
        ctrl.initialize();
        TreeItem<Resource> root = ctrl.getTree().getRoot();
        assertNotNull(root);

        ProjectConfig config = ProjectConfig.load(tmpPath.toString());
        ps.setConfig(config);

        assertNotEquals(ctrl.getTree().getRoot(), root);
        */
    }
}