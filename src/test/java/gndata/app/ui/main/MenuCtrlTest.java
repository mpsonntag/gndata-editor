package gndata.app.ui.main;

import java.io.File;
import java.nio.file.*;

import static org.junit.Assert.*;

import gndata.app.state.*;
import gndata.lib.config.*;
import org.apache.commons.io.FileUtils;
import org.junit.*;

public class MenuCtrlTest {

    private static final Path tmpConf = Paths.get(System.getProperty("java.io.tmpdir"), "test.json");
    private static final Path tmpPath = Paths.get(System.getProperty("java.io.tmpdir"), "test-project");

    TestableMenuCtrl ctrl;
    AppState appState;
    ProjectState projectState;

    @Before
    public void setUp() throws Exception {
        appState = new AppState();
        appState.setConfig(GlobalConfig.load(tmpConf.toString()));
        projectState = new ProjectState();
        ctrl = new TestableMenuCtrl(appState, projectState);
    }

    @After
    public void tearDown() throws Exception {
        if (Files.exists(tmpPath)) {
            FileUtils.deleteDirectory(tmpPath.toFile());
        }
        if (Files.exists(tmpConf)) {
            Files.delete(tmpConf);
        }
    }

    @Test
    public void testCreateProject() throws Exception {
        assertFalse(projectState.isConfigured());

        ctrl.createProject();

        assertTrue(projectState.isConfigured());

        assertEquals("OtherName", projectState.getConfig().getName());
        assertEquals("OtherDescription", projectState.getConfig().getDescription());
    }

    @Test
    public void testOpenProject() throws Exception {
        assertFalse(projectState.isConfigured());

        ctrl.openProject();

        assertTrue(projectState.isConfigured());
    }

    @Test
    public void testProjectSettings() throws Exception {
        ProjectConfig config = ProjectConfig.load(tmpPath.toString());
        projectState.setConfig(config);

        assertNull(config.getName());
        assertNull(config.getDescription());

        ctrl.projectSettings();

        assertEquals("OtherName", projectState.getConfig().getName());
        assertEquals("OtherDescription", projectState.getConfig().getDescription());
    }

    @Test
    public void testExit() throws Exception {
        assertTrue(appState.isRunning());
        ctrl.exit();
        assertFalse(appState.isRunning());
    }

    /**
     * A better testable version of MenuCtrl.
     */
    private static class TestableMenuCtrl extends MenuCtrl {

        public TestableMenuCtrl(AppState appState, ProjectState projectState) {
            super(appState, projectState);
        }

        @Override
        public File showDirectoryChooser() {
            return tmpPath.toFile();
        }

        @Override
        public ProjectConfig showConfigDialog(ProjectConfig config) {
            config.setName("OtherName");
            config.setDescription("OtherDescription");
            return config;
        }

        @Override
        public String showListDialog(GlobalConfig config) {
            return tmpPath.toString();
        }
    }
}