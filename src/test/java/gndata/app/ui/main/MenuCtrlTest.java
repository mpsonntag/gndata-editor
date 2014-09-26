package gndata.app.ui.main;

import gndata.app.state.AppState;
import gndata.app.state.ProjectState;
import gndata.lib.config.GlobalConfig;
import gndata.lib.config.ProjectConfig;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

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
        assertFalse(ctrl.calledShowChooser);
        assertFalse(ctrl.calledShowConfig);

        ctrl.createProject();

        assertTrue(projectState.isConfigured());
        assertTrue(ctrl.calledShowChooser);
        assertTrue(ctrl.calledShowConfig);

        assertEquals("OtherName", projectState.getConfig().getName());
        assertEquals("OtherDescription", projectState.getConfig().getDescription());
    }

    @Test
    public void testOpenProject() throws Exception {
        // TODO write test
        ctrl.openProject();
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

        public boolean calledShowChooser, calledShowConfig;

        public TestableMenuCtrl(AppState appState, ProjectState projectState) {
            super(appState, projectState);
            calledShowChooser = calledShowConfig = false;
        }

        @Override
        public File showDirectoryChooser() {
            calledShowChooser = true;
            return tmpPath.toFile();
        }

        @Override
        public ProjectConfig showConfigDialog(ProjectConfig config) {
            calledShowConfig = true;
            config.setName("OtherName");
            config.setDescription("OtherDescription");
            return config;
        }
    }
}