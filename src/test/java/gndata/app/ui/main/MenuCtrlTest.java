// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

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
import java.util.Optional;

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
        protected Optional<File> showDirectoryChooser() {
            return Optional.of(tmpPath.toFile());
        }

        @Override
        protected Optional<ProjectConfig> showConfigDialog(ProjectConfig config) {
            config.setName("OtherName");
            config.setDescription("OtherDescription");
            return Optional.of(config);
        }

        @Override
        protected Optional<String> showListDialog(GlobalConfig config) {
            return Optional.of(tmpPath.toString());
        }
    }
}